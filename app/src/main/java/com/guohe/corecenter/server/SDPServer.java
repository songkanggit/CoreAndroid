package com.guohe.corecenter.server;

import com.guohe.corecenter.constant.CameraDeviceConst;
import com.guohe.corecenter.core.logger.Logger;
import com.jieli.lib.dv.control.DeviceClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Description:
 * Author:created by bob on 17-7-5.
 */
public class SDPServer extends Thread {
    private String tag = getClass().getSimpleName();
    private ServerSocket mServerSocket;
    private boolean mRunning = false;
    private int mVideoFormat = DeviceClient.RTS_H264;
    private int mSampleRate = CameraDeviceConst.CAMERA_DEFAULT_SAMPLE;
    private int mFramePerSecond = CameraDeviceConst.VIDEO_FRAME_RATE_DEFAULT;
    private int mRtpVideoPort = CameraDeviceConst.RTP_VIDEO_PORT1;
    private int mRtpAudioPort = CameraDeviceConst.RTP_AUDIO_PORT1;

    public SDPServer(int port) {
        this(port, DeviceClient.RTS_H264);
    }
    public SDPServer(int port, int videoFormat) {
        mVideoFormat = videoFormat;
        try {
            mServerSocket = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mServerSocket != null) {
            try {
                mServerSocket.setReuseAddress(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            try {
                mServerSocket.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSampleRate(int sampleRate) {
        mSampleRate = sampleRate;
    }

    public void setFrameRate(int frameRate) {
        mFramePerSecond = frameRate;
    }

    public void stopRunning() {
        mRunning = false;
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mServerSocket = null;
    }
    public void setRtpVideoPort(int videoPort) {
        mRtpVideoPort = videoPort;
    }

    public void setRtpAudioPort(int audioPort) {
        mRtpAudioPort = audioPort;
    }

    @Override
    public void run() {
        super.run();
        mRunning = true;
        String format;
        int payload_type;
        if (mVideoFormat == DeviceClient.RTS_H264) {
            format = "H264";
            payload_type = 96;
        } else {
            format = "JPEG";
            payload_type = 26;
        }
        String sdp =
                "c=IN IP4 127.0.0.1\n" +
                "m=audio "+mRtpAudioPort+" RTP/AVP 97\n" +
                "a=rtpmap:97 L16/" + mSampleRate + "/1\n" +
                //"a=ptime:"+(1000/(mFramePerSecond !=0?mFramePerSecond:20))+"\n" +
                "a=ptime:20" + "\n" +
                "m=video "+mRtpVideoPort+" RTP/AVP " + payload_type + "\n" +
                "a=rtpmap:"+payload_type+" "+format+"/90000\n" +
                "a=framerate:" + mFramePerSecond;
        Logger.d(tag, "SDP:\n" +sdp);
        while (mRunning) {
            if (mServerSocket != null) {
                Socket connectionSocket = null;
                try {
                    connectionSocket = mServerSocket.accept();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                final Socket finalConnectionSocket = connectionSocket;
                if (finalConnectionSocket != null) {
                    try {
                        finalConnectionSocket.getOutputStream().write(sdp.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        finalConnectionSocket.getOutputStream().flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        finalConnectionSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //break;
                }
            }
        }
        stopRunning();
        Logger.d(tag, "SDP Server exit.");
    }
}
