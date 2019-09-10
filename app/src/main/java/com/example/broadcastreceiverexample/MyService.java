package com.example.broadcastreceiverexample;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static java.lang.Thread.sleep;

public class MyService extends IntentService {
    public MyService() {
        super("service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            sleep(5000);
            sendBroadcast(new Intent().setAction(MainActivity.MY_ACTION).putExtra("time",System.currentTimeMillis()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
