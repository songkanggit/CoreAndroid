package com.guohe.corecenter.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.guohe.corecenter.R;

/**
 * Created by kousou on 2019/1/8.
 */

public class NotificationUtil {
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private Notification.Builder mBuilder;
    private NotificationCompat.Builder mCompactBuilder;
    private Context mContext;
    private boolean isInit;

    public static NotificationUtil newInstance(Context context) {
        return new NotificationUtil(context);
    }

    private NotificationUtil(Context context){
        isInit = false;
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void initNotification(final String notificationId, final String notificationName, boolean isMute) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            if(!isMute) {
                notificationChannel.canShowBadge();
                notificationChannel.canBypassDnd();
                notificationChannel.shouldShowLights();
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationChannel.setBypassDnd(true);
                notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                notificationChannel.getAudioAttributes();
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100});
            }
            mNotificationManager.createNotificationChannel(notificationChannel);
            mBuilder = new Notification.Builder(mContext)
                    .setChannelId(notificationId)
                    .setContentTitle(notificationName)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round))
                    .setWhen(System.currentTimeMillis())
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setAutoCancel(true);
        } else {
            mCompactBuilder = new NotificationCompat.Builder(mContext)
                    .setContentTitle(notificationName)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(false)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher_round))
                    .setWhen(System.currentTimeMillis())
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setAutoCancel(true);
            if(!isMute) {
                mCompactBuilder.setVibrate(new long[]{100, 200, 100});
                mCompactBuilder.setDefaults(Notification.DEFAULT_ALL);
            }
        }
        isInit = true;
    }



    public void setContentText(final String contentText) {
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setContentText(contentText);
            } else {
                mCompactBuilder.setContentText(contentText);
            }
        }
    }

    public void setContentTitle(final String contentTitle) {
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setContentTitle(contentTitle);
            } else {
                mCompactBuilder.setContentTitle(contentTitle);
            }
        }
    }

    public void setContentIntent(PendingIntent pendingIntent) {
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setFullScreenIntent(null, true);
            } else {
                mCompactBuilder.setContentIntent(pendingIntent);
                mCompactBuilder.setFullScreenIntent(null, true);
            }
        }
    }

    public void setAutoCancel(boolean autoCancel) {
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setAutoCancel(false);
            } else {
                mCompactBuilder.setAutoCancel(true);
            }
        }
    }

    public void setProgress(final int progress) {
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBuilder.setProgress(100, progress, false);
            } else {
                mCompactBuilder.setProgress(100, progress, false);
            }
        }
    }

    public void showNotify(){
        if(isInit) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotification = mBuilder.build();
            } else {
                mNotification = mCompactBuilder.build();
            }
            mNotificationManager.notify(1, mNotification);
        }
    }
}
