package com.stepintech.driverapp.drivertracking;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.stepintech.driverapp.drivertracking.utils.TestJobService;
import com.stepintech.driverapp.drivertracking.utils.Util;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 1;
    private String TAG="MainActivity";
    Button button,button1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());
        button=findViewById(R.id.button);
        button1=findViewById(R.id.button2);

       /* Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash();
                Crashlytics.setUserIdentifier(getString(R.string.transport_id));//Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/


        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            // Check GPS is enabled
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(MainActivity.this, "Please enable location services", Toast.LENGTH_SHORT).show();
                finish();
            }

            // Check location permission is granted - if it is, start
            // the service, otherwise request the permission
            int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {


              //  scheduleJob();
                    Toast.makeText(MainActivity.this, "isServiceRunning11..", Toast.LENGTH_SHORT).show();

                    startTrackerService();
                 Util.scheduleJob(MainActivity.this);




            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);
            }
        }
    });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MediaPlayer thePlayer = MediaPlayer.create(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.samlemp3);
//                mediaPlayer.start();
                Util.stopMyJob(MainActivity.this);
            }
        });

    }

    private void startTrackerService() {

        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(new Intent(this, TrackingService.class));
                else
                    startService(new Intent(this, TrackingService.class));

            }else {
                //No internet
                Toast.makeText(MainActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            }
        } else {
            //No internet
            Toast.makeText(MainActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();

        }


        //finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted

            startTrackerService();
            Util.scheduleJob(MainActivity.this);
            //onStartJobIntentService();
        } else {
            finish();
        }
    }



}