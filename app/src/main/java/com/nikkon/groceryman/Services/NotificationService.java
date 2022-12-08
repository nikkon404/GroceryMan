package com.nikkon.groceryman.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class NotificationService {
    public static final String CHANNEL_ID = "grocery_channel";
    private static final String CHANNEL_NAME = "Grocery Channel";
    private static final String CHANNEL_DESC = "Grocery Channel for Grocery App";
    public static final int NOTIFICATION_ID = 400;

    private Context context;

    // NotificationManager is a system service that you can use to notify the user that something has happened in the background.
    private NotificationManager manager;

    //singleton
    private static NotificationService instance;

    public static NotificationService getInstance(Context context){
        if(instance == null){
            instance = new NotificationService(context);
        }
        return instance;
    }

    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);

            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

//      Calendar calendar = Calendar.getInstance();
//        //10 is for how many seconds from now you want to schedule also you can create a custom instance of Callender to set on exact time
//        calendar.add(Calendar.SECOND, 5);
//        NotificationService.getInstance(getApplicationContext()).scheduleNotification(calendar.getTimeInMillis(), "Milk");
//src:      https://riptutorial.com/android/example/11495/scheduling-notifications
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scheduleNotification(Long timeMilliSec, String groceryName) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", groceryName);
        intent.putExtra("desc", "Grocery is about to expire!!");
        PendingIntent pending = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // create alarm manager
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMilliSec, pending);
        Toast.makeText(context, "Scheduled ", Toast.LENGTH_LONG).show();
    }



}
