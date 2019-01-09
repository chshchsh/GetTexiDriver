package com.jct.bd.gettexidriver.model.backend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.Activities.LoginActivity;

import static android.support.v4.content.ContextCompat.getColor;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        initChannels(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, LoginActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
                builder.setContentTitle(context.getString(R.string.relevent_ride))
                .setContentText(context.getString(R.string.new_reqest));
        builder.setContentIntent(contentIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_directions_car_black_24dp);
            builder.setColor(getColor(context, R.color.bg_login));
        } else {
            builder.setSmallIcon(R.drawable.ic_directions_car_black_24dp);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
    public  void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default", "Channel name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }
}
