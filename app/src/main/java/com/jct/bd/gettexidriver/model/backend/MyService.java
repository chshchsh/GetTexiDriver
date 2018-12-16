package com.jct.bd.gettexidriver.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
            SumCalc sumCalc = new SumCalc();
            sumCalc.start();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG," onDestroy");

    }
    public class SumCalc extends Thread {

        public void run() {

            isThreadOn = false;

        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
