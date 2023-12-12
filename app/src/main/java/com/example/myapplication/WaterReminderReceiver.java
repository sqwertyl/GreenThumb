package com.example.myapplication;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class WaterReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "GreenThumb")
                .setSmallIcon(R.drawable.ic_plant)
                .setContentTitle("GreenThumb")
                .setContentText("It's time to water your plants!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        nManager.notify(1, builder.build());
        Log.d("GreenThumb: WaterReminderReceiver", "Attempted sent notif");
    }
}
