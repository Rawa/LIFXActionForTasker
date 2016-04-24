package com.rawa.tasker.lifxplugin.lifx;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.rawa.tasker.lifxplugin.R;


/**
 * Created by rawa on 2015-06-16.
 */
public class NotificationHelper {
    private final static String tag = "NotificationHelper";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void notification(Context context, String title, String content){
        Notification n;
        Notification.Builder builder =
                new Notification.Builder(context)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            n = builder.getNotification();
        } else {
            n = builder.setStyle(new Notification.BigTextStyle(builder)).build();
        }

        //Push notification to user
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
