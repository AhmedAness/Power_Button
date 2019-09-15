package com.example.dt.powerbutton;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class LockService extends Service {

    BroadcastReceiver mReceiver;
    String CHANNEL_ID = "my_channel_02";
    NotificationChannel mChannel;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sendNotification();
    }


    public void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("notification")
                        .setContentText("service in back!");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            mBuilder.setChannelId(CHANNEL_ID);
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
        startForeground(3, mBuilder.build());
//        mNotificationManager.notify(3, mBuilder.build());


    }
//    @Override
//    public void onStart(Intent intent, int startid) {
//        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_USER_PRESENT);
//        mReceiver = new ScreenReceiver();
//        registerReceiver(mReceiver, filter);
//        Log.d("Anis", "hi man it's service");
//    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        Log.d("Aniss", filter.getAction(0)+" "+filter.getAction(0));
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocalBinder extends Binder {
        LockService getService() {
            return LockService.this;
        }
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, LockService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent);
        } else {
            this.startService(intent);
        }
        Log.d("Anis", "onDestroy");
    }
}
