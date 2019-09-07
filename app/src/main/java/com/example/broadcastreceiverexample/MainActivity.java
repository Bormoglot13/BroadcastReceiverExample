package com.example.broadcastreceiverexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String MY_ACTION = "MY_ACTION";
    TextView tv_log;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_log = findViewById(R.id.tv_log);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},1111);
        } else {
            registerMyReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            registerMyReceiver();
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    void registerMyReceiver (){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MY_ACTION);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_CALL);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tv_log.setText(tv_log.getText() + " получен новый интент " + intent.getAction() + "\n");
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                   StringBuilder stringBuilder = new StringBuilder();
                   if (intent.getExtras() != null){
                       Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                       SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                       for (int i = 0;i < pdus.length;++i){
                           smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                           stringBuilder.append("\nMessage:" + smsMessages[i].getDisplayMessageBody());
                           stringBuilder.append("\nauthor:" + smsMessages[i].getDisplayOriginatingAddress());
                           stringBuilder.append("\nauthor:" + smsMessages[i].getOriginatingAddress());
                       }
                    }
                    tv_log.setText(tv_log.getText() + stringBuilder.toString());
                }

            }
        };



   }

}



