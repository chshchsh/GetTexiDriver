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
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context,MainActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.taxi)
                .setContentTitle("new request for drive")
                .setContentText("you have a new relevant ride to take");
        builder.setContentIntent(contentIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
}
