package com.stepintech.driverapp.drivertracking.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.stepintech.driverapp.drivertracking.MainActivity;
import com.stepintech.driverapp.drivertracking.TrackingService;

import java.util.Calendar;

import androidx.annotation.RequiresApi;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class Util {
   static JobScheduler jobScheduler;
    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        Log.d("scheduleJob","scheduleJob1100");
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
      //  builder.setMinimumLatency(max(1, nextTime.getTime() - System.currentTimeMillis()));;
       // builder.setOverrideDeadline(15 * 1000);

        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not




        if(!isServiceRunning(TrackingService.class,context))
        {
           // Toast.makeText(context, "isServiceRunning11..", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                 jobScheduler = context.getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
            }
            else {
                Intent myIntent = new Intent(context, TestJobService.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 1 * 1000, pendingIntent);
            }

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void stopMyJob(Context context) {
        jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE );
        jobScheduler.cancelAll();
    }

    private static boolean isServiceRunning(Class<?> serviceClass,Context context) {
        Log.d("isServiceRunning","");
        ActivityManager manager;
        manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }


}
