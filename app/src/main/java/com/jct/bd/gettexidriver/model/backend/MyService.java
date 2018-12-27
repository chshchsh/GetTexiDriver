package com.jct.bd.gettexidriver.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jct.bd.gettexidriver.controller.MainActivity;
import com.jct.bd.gettexidriver.model.datasource.NotifyDataChange;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.List;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext()," Service Create",Toast.LENGTH_LONG);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
                FactoryBackend.getInstance().notifyToRideList(new NotifyDataChange<List<Ride>>() {
                    @Override
                    public void OnDataChanged(List<Ride> obj) {
                        Intent intent = new Intent(MyService.this,MainActivity.class);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getBaseContext(), "error to get rides list\n" + exception.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FactoryBackend.getInstance().stopNotifyToRidesList();
        Toast.makeText(getApplicationContext()," Service destroy",Toast.LENGTH_LONG);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
