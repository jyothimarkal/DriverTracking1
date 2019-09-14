package com.stepintech.driverapp.drivertracking.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import com.stepintech.driverapp.drivertracking.TrackingService;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {


       // Intent service = new Intent(getApplicationContext(), TrackingService.class);
       // getApplicationContext().startService(service);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(new Intent(this, TrackingService.class));
        else
            startService(new Intent(this, TrackingService.class));


        // Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
