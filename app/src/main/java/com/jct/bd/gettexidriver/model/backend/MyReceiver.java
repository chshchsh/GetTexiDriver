package com.jct.bd.gettexidriver.model.backend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.jct.bd.gettexidriver.R;
import com.jct.bd.gettexidriver.controller.LoginActivity;
import com.jct.bd.gettexidriver.controller.MainActivity;
import com.jct.bd.gettexidriver.controller.availableRiedsFragment;

import static android.support.v4.content.ContextCompat.getColor;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, LoginActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.relevent_ride))
                .setContentText(context.getString(R.string.new_reqest));
        builder.setContentIntent(contentIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_directions_car_black_24dp);
            builder.setColor(getColor(context,R.color.bg_login));
        } else {
            builder.setSmallIcon(R.drawable.drive);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
}
