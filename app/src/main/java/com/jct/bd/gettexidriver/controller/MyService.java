package com.jct.bd.gettexidriver.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public final String TAG = "myService";

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return flags;
    }
    @Override
    public void onDestroy() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
