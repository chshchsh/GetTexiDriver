package com.jct.bd.gettexidriver.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jct.bd.gettexidriver.controller.MainActivity;
import com.jct.bd.gettexidriver.model.datasource.FireBase_DB_manager;
import com.jct.bd.gettexidriver.model.entities.Ride;

import java.util.List;

public class MyService extends Service {
    boolean isThreadOn = false;
    public final String TAG = "myService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG," onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isThreadOn)
        {
            isThreadOn = true;
            Listener listener = new Listener();
            listener.start();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FireBase_DB_manager.stopNotifyToRidesList();
    }
    public class Listener extends Thread {

        public void run() {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FireBase_DB_manager.notifyToRideList(new FireBase_DB_manager.NotifyDataChange<List<Ride>>() {
                @Override
                public void OnDataChanged(List<Ride> obj) {
                    Intent intent = new Intent(MyService.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(intent);
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "error to get rides list\n" + exception.toString(), Toast.LENGTH_LONG).show();
                }
            });
            isThreadOn = false;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
