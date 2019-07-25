package com.guohe.corecenter.view;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kousou on 2019/3/26.
 */

public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private OnFocusListener onFocusListener;

    private boolean needToTakePic = false;

    private int mCameraId;

    private Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0) {
                mCamera.cancelAutoFocus();
            }
        }
    };

    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, int cameraId) {
        super(context);
        this.mCameraId = cameraId;
        this.mCamera = Camera.open(mCameraId);
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.onFocusListener = (OnFocusListener) context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        this.mSurfaceHolder.removeCallback(this);
        mCamera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // intentionally left blank for a test
            e.printStackTrace();
        }
    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param - Rect - new area for auto focus
     */
    public void doTouchFocus(final Rect tfocusRect) {
        try {
            if(mCamera.getParameters().getMaxNumFocusAreas() > 0 && mCamera.getParameters().getMaxNumMeteringAreas() > 0) {
                List<Camera.Area> focusList = new ArrayList<>();
                Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
                focusList.add(focusArea);

                Camera.Parameters param = mCamera.getParameters();
                param.setFocusAreas(focusList);
                param.setMeteringAreas(focusList);
                mCamera.setParameters(param);
            }

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isNeedToTakePic()) {
            onFocusListener.onFocused();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int) (x - 100),
                    (int) (y - 100),
                    (int) (x + 100),
                    (int) (y + 100));


            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000 / this.getWidth() - 1000,
                    touchRect.top * 2000 / this.getHeight() - 1000,
                    touchRect.right * 2000 / this.getWidth() - 1000,
                    touchRect.bottom * 2000 / this.getHeight() - 1000);

            doTouchFocus(targetFocusRect);
        }

        return false;
    }

    public boolean isNeedToTakePic() {
        return needToTakePic;
    }

    public void setNeedToTakePic(boolean needToTakePic) {
        this.needToTakePic = needToTakePic;
    }

    public boolean changeCameraSide() {
        int cameraCount = Camera.getNumberOfCameras();
        if(cameraCount == 2) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            if(mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else {
                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            mCamera = Camera.open(mCameraId);
            return initCamera(getHolder());
        }
        return false;
    }

    public Camera getCamera() {
        return mCamera;
    }


    private boolean initCamera(SurfaceHolder surfaceHolder) {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
            mCamera.setDisplayOrientation(90);
            Camera.Size bestSize = calculatePerfectSize(mCamera.getParameters().getSupportedPreviewSizes(), getWidth(), getHeight());
            Camera.Size pictureSize = calculatePerfectSize(mCamera.getParameters().getSupportedPictureSizes(), bestSize.width, bestSize.height);
            parameters.setPreviewSize(bestSize.width, bestSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth, int expectHeight) {
        sortList(sizes); // 根据宽度进行排序
        Camera.Size result = sizes.get(0);
        boolean widthOrHeight = false; // 判断存在宽或高相等的Size
        // 辗转计算宽高最接近的值
        for (Camera.Size size: sizes) {
            // 如果宽高相等，则直接返回
            if (size.width == expectWidth && size.height == expectHeight) {
                result = size;
                break;
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (size.width == expectWidth) {
                widthOrHeight = true;
                if (Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (size.height == expectHeight) {
                widthOrHeight = true;
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)) {
                    result = size;
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)
                        && Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
        }
        return result;
    }

    private void sortList(List<Camera.Size> list) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size pre, Camera.Size after) {
                if (pre.width > after.width) {
                    return 1;
                } else if (pre.width < after.width) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public interface OnFocusListener {
        void onFocused();
    }
}
