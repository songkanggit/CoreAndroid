package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.guohe.corecenter.R;
import com.guohe.corecenter.constant.CameraDeviceConst;
import com.guohe.corecenter.core.logger.Logger;
import com.guohe.corecenter.server.SDPServer;
import com.guohe.corecenter.widget.IjkVideoView;
import com.gyf.immersionbar.ImmersionBar;
import com.jieli.lib.dv.control.DeviceClient;
import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.connect.response.SendResponse;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.player.OnRealTimeListener;
import com.jieli.lib.dv.control.player.RealtimeStream;
import com.jieli.lib.dv.control.player.Stream;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import com.jieli.lib.dv.control.utils.Code;
import com.jieli.lib.dv.control.utils.Constants;
import com.jieli.lib.dv.control.utils.Topic;
import com.jieli.lib.dv.control.utils.TopicKey;

public class DeviceControlActivity extends BaseActivity implements OnNotifyListener, View.OnClickListener {
    public static final String WIFI_PREFIX = "wifi_camera_";
    private static final String DEFAULT_VIDEO_STREAM_ADDRESS = "tcp://127.0.0.1:6789";

    private static final int MSG_HEART_BEAT_FAILED = 10;
    private static final int MSG_DEVICE_DISCONNECTED = 11;
    private static final int MSG_WIFI_DISCONNECTED = 12;

    private DeviceClient mDeviceClient;
    private RealtimeStream mRealTimeStream;
    private WifiBroadcastReceiver mWifiStateReceiver;
    private IntentFilter mIntentFilter;
    private IjkVideoView mVideoView;
    private SDPServer mLiveSever;

    private Handler mHandler = new Handler(message -> {
        switch (message.what) {
            case MSG_HEART_BEAT_FAILED:
            case MSG_DEVICE_DISCONNECTED: {
                finish();
                break;
            }
            case MSG_WIFI_DISCONNECTED: {

                break;
            }
        }
        return false;
    });

    private boolean isSentOpenRtsCmd = false;

    /**
     * 默认每隔5秒发送一次心跳数据
     */
    private static final int DEFAULT_HEARTBEAT_PERIOD = 5 * 1000;//5S
    private boolean keepSendHeartBeat = true;
    private volatile int mHeartBeatCount;
    private final int mMaxSendTimes = 5;
    private Runnable mHeartBeat = new Runnable() {
        @Override
        public void run() {
            keepSendHeartBeat = true;
            mHeartBeatCount = 0;
            while (keepSendHeartBeat) {
                if(mDeviceClient != null) {
                    mDeviceClient.tryToKeepAlive(code -> {
                        if(code != CameraDeviceConst.SEND_SUCCESS) {
                            Logger.d(TAG, "Heart beat send failed.");
                        }
                    });
                    SystemClock.sleep(DEFAULT_HEARTBEAT_PERIOD);
                    mHeartBeatCount ++;
                    if(mHeartBeatCount >= mMaxSendTimes) {
                        keepSendHeartBeat = false;
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_HEART_BEAT_FAILED));
                        Logger.d(TAG, "Heart beat send failed.");
                    }
                }
            }
        }
    };

    protected void parseNonNullBundle(Bundle bundle){}
    protected void initDataIgnoreUi() {
        mDeviceClient = new DeviceClient(getApplication(), DeviceClient.PROTOCOL_TCP);
        mDeviceClient.registerConnectStateListener(mOnConnectStateListener);
        mDeviceClient.registerNotifyListener(this::onNotify);
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_device_control;}
    protected void viewAffairs(){
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).statusBarDarkFont(true).fitsSystemWindows(true).init();
        mVideoView = fvb(R.id.video_view);
    }
    protected void assembleViewClickAffairs(){
//        mBeginTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mWifiStateReceiver = new WifiBroadcastReceiver();
        registerReceiver(mWifiStateReceiver, mIntentFilter);

        tryAccessDevice();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(keepSendHeartBeat) {
            keepSendHeartBeat = false;
            mCoreContext.removeExecuteAsyncTask(mHeartBeat);
        }
        tryCloseRTS();
        stopRdpServer();
        if(mDeviceClient != null) {
            mDeviceClient.unregisterConnectStateListener(mOnConnectStateListener);
            mDeviceClient.unregisterNotifyListener(this::onNotify);
            mDeviceClient.release();
            mDeviceClient = null;
        }
        unregisterReceiver(mWifiStateReceiver);
    }

    @Override
    public void onNotify(NotifyInfo notifyInfo) {
        final String topic = notifyInfo.getTopic();
        Logger.d(TAG, "Topic:" + topic);
        if(notifyInfo.getErrorType() != Code.ERROR_NONE) {
            Logger.d(TAG, "onNotify Error:" + notifyInfo.getErrorType());
            return;
        }
        switch (topic) {
            case Topic.PULL_VIDEO_STATUS: {

                break;
            }
            case Topic.KEEP_ALIVE: {
                mHeartBeatCount = 0;
                break;
            }
            case Topic.OPEN_FRONT_RTS: {
                if(isSentOpenRtsCmd && notifyInfo.getParams() != null) {
                    startRdpServer(notifyInfo);

                    isSentOpenRtsCmd = false;
                    int width, height;
                    int format = -1;
                    boolean isFrontCamera = true;
                    width = height = 0;
                    String rtsWidth = notifyInfo.getParams().get(TopicKey.WIDTH);
                    String rtsHeight = notifyInfo.getParams().get(TopicKey.HEIGHT);
                    String rtsFormat = notifyInfo.getParams().get(TopicKey.FORMAT);
                    if(!TextUtils.isEmpty(rtsWidth) && TextUtils.isDigitsOnly(rtsWidth)){
                        width = Integer.valueOf(rtsWidth);
                    }
                    if(!TextUtils.isEmpty(rtsHeight) && TextUtils.isDigitsOnly(rtsHeight)){
                        height = Integer.valueOf(rtsHeight);
                    }
                    if(!TextUtils.isEmpty(rtsFormat) && TextUtils.isDigitsOnly(rtsFormat)){
                        format = Integer.valueOf(rtsFormat);
                    }

                    if (format == DeviceClient.RTS_JPEG) {
                        mRealTimeStream.setResolution(width, height);
                    }
                    if (isFrontCamera) {
                        mRealTimeStream.setFrameRate(CameraDeviceConst.CAMERA_FRONT_RATE);
                        mRealTimeStream.setSampleRate(CameraDeviceConst.CAMERA_FRONT_SAMPLE);
                    } else {
                        mRealTimeStream.setFrameRate(CameraDeviceConst.CAMERA_REAR_RATE);
                        mRealTimeStream.setSampleRate(CameraDeviceConst.CAMERA_REAR_SAMPLE);
                    }

                    mHandler.postDelayed(() -> mVideoView.setVideoPath(DEFAULT_VIDEO_STREAM_ADDRESS), 500);
                }
                break;
            }
        }
    }

    private void tryAccessDevice() {
        if(!mDeviceClient.isConnected()) {
            WifiManager wifiService = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiService == null) {
                Logger.d(TAG, "Get Wifi Manager failed.");
                return;
            }
            WifiInfo wifiInfo = wifiService.getConnectionInfo();
            if(wifiInfo == null) {
                Logger.d(TAG, "Get Wifi info failed.");
                return;
            }
            final String ssid = wifiInfo.getSSID();
            if(ssid.contains(WIFI_PREFIX)) {
                DhcpInfo dhcpInfo = wifiService.getDhcpInfo();
                final String ip = Formatter.formatIpAddress(dhcpInfo.gateway);
                mDeviceClient.create(ip, 3333);
                Logger.d(TAG, "Device ip has established." + ip);
            } else {
                Logger.d(TAG, "Wifi is not aimed device." + ssid);
            }
        }
    }

    private void tryOpenRTS(int mode, int port) {
        mDeviceClient.tryToOpenRTStream(CameraDeviceConst.CAMERA_TYPE_FRONT, CameraDeviceConst.CAMERA_FORMAT_H264, 1920, 1080, CameraDeviceConst.VIDEO_FRAME_RATE_DEFAULT, new SendResponse() {
            @Override
            public void onResponse(Integer code) {
                isSentOpenRtsCmd = true;
                if(code == 1) {//SUCCESS
                    if(mRealTimeStream == null) {
                        mRealTimeStream = new RealtimeStream(mode);
                        mRealTimeStream.registerStreamListener(mOnRealTimeListener);
                    }
                    if(!mRealTimeStream.isReceiving()) {
                        if (mode == Stream.Protocol.TCP_MODE) {
                            mRealTimeStream.create(port, mDeviceClient.getAddress());
                        } else {
                            mRealTimeStream.create(port);
                        }
                        mRealTimeStream.setSoTimeout(5000);
                        mRealTimeStream.useDeviceTimestamp(true);
                        mRealTimeStream.configure(CameraDeviceConst.RTP_VIDEO_PORT1, CameraDeviceConst.RTP_AUDIO_PORT1);

                        mCoreContext.executeAsyncTask(mHeartBeat);
                    }
                } else {
                    Logger.d(TAG, "Failed to create RTS.");
                }
            }
        });
    }

    private void tryCloseRTS() {
        if(mRealTimeStream != null) {
            mRealTimeStream.unregisterStreamListener(mOnRealTimeListener);
            mRealTimeStream.close();
            mRealTimeStream = null;
        }
    }

    private void startRdpServer(NotifyInfo notifyInfo) {
        if (mLiveSever == null) {
            int frameRate = CameraDeviceConst.VIDEO_FRAME_RATE_DEFAULT;
            int sampleRate = CameraDeviceConst.CAMERA_DEFAULT_SAMPLE;
            int format = -1;
            String rtsFormat = notifyInfo.getParams().get(TopicKey.FORMAT);
            String rtsFrameRate = notifyInfo.getParams().get(TopicKey.FRAME_RATE);
            String rtsSampleRate = notifyInfo.getParams().get(TopicKey.SAMPLE);
            if(!TextUtils.isEmpty(rtsSampleRate) && TextUtils.isDigitsOnly(rtsSampleRate)){
                sampleRate = Integer.valueOf(rtsSampleRate);
            }
            if(!TextUtils.isEmpty(rtsFrameRate) && TextUtils.isDigitsOnly(rtsFrameRate)){
                frameRate = Integer.valueOf(rtsFrameRate);
            }
            if(!TextUtils.isEmpty(rtsFormat) && TextUtils.isDigitsOnly(rtsFormat)){
                format = Integer.valueOf(rtsFormat);
            }
            mLiveSever = new SDPServer(CameraDeviceConst.RDP_SERVER_PORT, format);
            mLiveSever.setFrameRate(frameRate);
            mLiveSever.setSampleRate(sampleRate);
            Logger.i(TAG, "format " + format +", frameRate=" + frameRate +", sampleRate=" + sampleRate);
            mLiveSever.setRtpVideoPort(CameraDeviceConst.RTP_VIDEO_PORT1);
            mLiveSever.setRtpAudioPort(CameraDeviceConst.RTP_AUDIO_PORT1);
            mLiveSever.start();
        }
    }

    private void stopRdpServer() {
        if(mLiveSever != null) {
            mLiveSever.stopRunning();
            mLiveSever = null;
        }
    }

    private OnRealTimeListener mOnRealTimeListener = new OnRealTimeListener() {
        @Override
        public void onVideo(int i, int i1, byte[] bytes, long l, long l1) {
            Logger.d(TAG, "onVideo");
        }

        @Override
        public void onAudio(int i, int i1, byte[] bytes, long l, long l1) {
            Logger.d(TAG, "onAudio");
        }

        @Override
        public void onStateChanged(int i) {
            Logger.d(TAG, "onStateChanged" + i);
        }

        @Override
        public void onError(int i, String s) {
            Logger.d(TAG, "onError" + s);
        }
    };

    private OnConnectStateListener mOnConnectStateListener = code -> {
        Logger.d(TAG, "Code:" + code);
        switch (code) {
            case Constants.DEVICE_STATE_CONNECTED: {
                tryOpenRTS(Stream.Protocol.TCP_MODE, CameraDeviceConst.VIDEO_SERVER_PORT);
                break;
            }
            case Constants.DEVICE_STATE_CONNECTION_TIMEOUT:
            case Constants.DEVICE_STATE_EXCEPTION: {
                break;
            }
            case Constants.DEVICE_STATE_DISCONNECTED:
            case Constants.DEVICE_STATE_UNREADY:{
                Toast.makeText(getApplicationContext(), "设备断开", Toast.LENGTH_SHORT).show();
                mHandler.sendMessage(mHandler.obtainMessage(MSG_DEVICE_DISCONNECTED));
                break;
            }
        }
    };

    private class WifiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if(networkInfo != null) {
                    boolean isWifiConnected = networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED;
                    if(!isWifiConnected) {
                        Logger.d(TAG, "Wifi is not Connected");
                        return;
                    }
                    tryAccessDevice();
                }
            }
        }
    }
}
