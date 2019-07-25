package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.guohe.corecenter.R;
import com.guohe.corecenter.core.logger.Logger;
import com.guohe.corecenter.zxing.camera.CameraManager;
import com.guohe.corecenter.zxing.decode.CaptureActivityHandler;
import com.guohe.corecenter.zxing.decode.InactivityTimer;
import com.guohe.corecenter.zxing.view.ViewfinderView;
import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {
    private static final float BEEP_VOLUME = 1f;

    private SurfaceView mSurfaceView;
    private ImageView mBackImageView, mFlashIV;
    private CaptureActivityHandler mCaptionHandler;
    private ViewfinderView mViewfinderView;
    private InactivityTimer mInactivityTimer;
    private MediaPlayer mMediaPlayer;
    private boolean mHasSurface;
    private boolean mVibrate;
    private boolean mPlayBeep;
    private boolean mFlashState;

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        askPermissions();
        mHasSurface = false;
        mFlashState = false;
        CameraManager.init(getApplication());
        mInactivityTimer = new InactivityTimer(this);
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_capture;}
    protected void viewAffairs() {
        ImmersionBar.with(this).transparentStatusBar().keyboardEnable(true).init();
        mFlashIV = fvb(R.id.iv_flash);
        mBackImageView = fvb(R.id.toolbar_back);
        mViewfinderView = fvb(R.id.viewfinder_content);
        mSurfaceView = fvb(R.id.scanner_view);
    }
    protected void assembleViewClickAffairs(){
        mBackImageView.setOnClickListener(this);
        mFlashIV.setOnClickListener(this);
    }
    protected void initDataAfterUiAffairs(){}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back: {
                finish();
                break;
            }
            case R.id.iv_flash: {
                if(mFlashState) {
                    mFlashIV.setImageResource(R.mipmap.flashlight_off);
                    CameraManager.get().closeFlashLight();
                } else {
                    mFlashIV.setImageResource(R.mipmap.flashlight_on);
                    CameraManager.get().openFlashLight();
                }
                mFlashState = !mFlashState;
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mPlayBeep = false;
        }
        initBeepSound();
        mVibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptionHandler != null) {
            mCaptionHandler.quitSynchronously();
            mCaptionHandler = null;
        }
        CameraManager.get().closeDriver();
        CameraManager.get().stopPreview();
        mVibrate = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInactivityTimer.shutdown();
        if(mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        handleScanResult(result.getText());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public Handler getHandler() {
        return mCaptionHandler;
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }

    private void handleScanResult(final String resultString) {
        playBeepSoundAndVibrate();
        runOnUiThread(() -> {
            if (TextUtils.isEmpty(resultString)) {
                mInactivityTimer.resetTimer();
                Toast.makeText(CaptureActivity.this, "扫描失败!", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultString);
                resultIntent.putExtras(bundle);
                CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            }
            CaptureActivity.this.finish();
        });
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            CameraManager.get().startPreview();
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (mCaptionHandler == null) {
            mCaptionHandler = new CaptureActivityHandler(this, null,
                    null);
        }
    }

    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> mMediaPlayer.seekTo(0));

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;
    private void playBeepSoundAndVibrate() {
        if (mPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    //Permissions Block
    private static final String[] REQUIRED_PERMISSIONS =
            {Manifest.permission.CAMERA, Manifest.permission.VIBRATE};
    private static final int RC_RW_STORAGE_PERM = 125;

    @AfterPermissionGranted(RC_RW_STORAGE_PERM)
    private void askPermissions() {
        if(hasRequiredPermissions()) {

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.main_rational_message), RC_RW_STORAGE_PERM, REQUIRED_PERMISSIONS);
        }
    }

    private boolean hasRequiredPermissions() {
        return EasyPermissions.hasPermissions(this, REQUIRED_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Logger.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Logger.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        finish();
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Logger.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Logger.d(TAG, "onRationaleDenied:" + requestCode);
    }
}
