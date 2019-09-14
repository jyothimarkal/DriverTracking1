package com.stepintech.driverapp.drivertracking.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.stepintech.driverapp.drivertracking.TrackingService;

import java.util.Calendar;

public class MyStartServiceReceiver extends BroadcastReceiver {
    Context context1;

    @Override
    public void onReceive(Context context, Intent intent) {
        context1=context;

        Log.d("inside my broadcast","works...");
/*
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnected())
        {
            Log.d("my broadcast","works");
            Toast.makeText(context,"BroadcastReceiver...working",Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.d("my broadcast","not works");
            Toast.makeText(context,"BroadcastReceiver...not working ",Toast.LENGTH_LONG).show();
        }*/
            if(!isServiceRunning(TrackingService.class))
            {
                Util.scheduleJob(context);
            }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager;
        manager = (ActivityManager) context1.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}