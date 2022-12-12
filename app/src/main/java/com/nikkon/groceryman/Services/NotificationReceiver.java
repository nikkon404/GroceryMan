package com.nikkon.groceryman.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.nikkon.groceryman.Activities.ItemDetailActivity;
import com.nikkon.groceryman.Activities.MainActivity;
import com.nikkon.groceryman.Models.Item;
import com.nikkon.groceryman.Models.ItemModel;
import com.nikkon.groceryman.R;

//src https://riptutorial.com/android/example/11495/scheduling-notifications
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", 0);

        Item item = (new ItemModel(context)).findItemById(id);
        Intent i = new Intent(context, ItemDetailActivity.class);
        i.putExtra("item", item);
        // Build notification based on Intent
        Notification notification = new  NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("desc"))
                .setContentIntent(PendingIntent.getActivity(context, 0, i, 0))
                .build();

        // Show notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NotificationService.NOTIFICATION_ID, notification);


    }

}