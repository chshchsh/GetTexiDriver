package com.jct.bd.gettexidriver.model.backend;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.MainActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        NotificationManager notificationManager;
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder b = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.taxi)
                .setContentTitle("new request for drive")
                .setContentText("you have a new relevant ride to take")
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");
        notificationManager.notify(1, b.build());
    }
}
