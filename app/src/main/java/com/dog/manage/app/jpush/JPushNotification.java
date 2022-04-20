package com.dog.manage.app.jpush;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.MainActivity;

import java.util.UUID;

import cn.jpush.android.api.CustomMessage;

public class JPushNotification {
    private static final String TAG = "JPushNotification";
    private static JPushNotification instance;
    private Context context;

    private JPushNotification(Context context) {
        this.context = context;
    }

    public static JPushNotification instance(Context context) {
        if (instance == null) {
            instance = new JPushNotification(context);
        }

        return instance;
    }

    private static final int NOTIFY_NEW_MSG = 90;

    NotificationManager notificationManager;

    // 将消息提示到通知栏
    public void notice(CustomMessage customMessage) {
        String title = !TextUtils.isEmpty(customMessage.title) ? customMessage.title : context.getString(R.string.app_name);
        String message = customMessage.message;
        String extras = customMessage.extra;
        String messageId = customMessage.messageId;

//        if (!CommonUtil.isForeground(context, "com.chonger.activity.IMChatActivity")) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.strong_message);
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(context.getPackageName() + ".chat", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mChannel.setSound(sound, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Builder(context)
                    .setChannelId(context.getPackageName() + ".chat")
                    .setTicker(context.getResources().getString(R.string.app_name))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
                    .build();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setTicker(context.getResources().getString(R.string.app_name));
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSmallIcon(R.mipmap.app_icon);
            builder.setAutoCancel(true);
//            builder.setContentIntent(pendingIntent);
            builder.setSound(sound);
            builder.setLights(Color.GREEN, 1000, 1000);
            notification = builder.build();
        }
        notificationManager.notify(NOTIFY_NEW_MSG, notification);
//        }
    }

    public void notificationCancelAll() {
        if (null != notificationManager) {
            notificationManager.cancelAll();
        }
    }
}
