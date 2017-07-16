package com.example.aarshad.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by aarshad on 7/16/17.
 * Class extends BroadcastReceiver which will execute once the phone is Re-booted
 */

public class RunWithOS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
          // this will be executed when the system starts

            GlobalInfo globalInfo = new GlobalInfo(context);
            globalInfo.loadData();

            if (!TrackLocation.isRunning){
                TrackLocation trackLocation = new TrackLocation();
                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, trackLocation);
            }
            if (!MyService.isRunning){
                Intent i = new Intent(context, MyService.class);
                context.startService(i);
            }


        }
    }
}
