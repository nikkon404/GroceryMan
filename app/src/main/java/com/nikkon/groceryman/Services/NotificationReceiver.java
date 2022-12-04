package com.nikkon.groceryman.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Build notification based on Intent
        Notification notification = new  NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("desc"))
                .build();
        // Show notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NotificationService.NOTIFICATION_ID, notification);
    }
}