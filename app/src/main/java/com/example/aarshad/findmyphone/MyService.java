package com.example.aarshad.findmyphone;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends IntentService {

    LatLng latLng;

    public static boolean isRunning = false ;
    DatabaseReference mDatabaseReference ;

    public MyService() {
        super("MyService");
        isRunning = true;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Updates")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GPSHelper gpsHelper = new GPSHelper(getApplicationContext());
                    if (gpsHelper.isGPSenabled()){

                         latLng = gpsHelper.getMyLocation();
                       // Toast.makeText(getApplicationContext(),"GPS Enabled & Location is : " + latLng.toString(),Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(getApplicationContext(),"GPS Disabled",Toast.LENGTH_SHORT).show();
                    }

                    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Location")
                            .child("lat").setValue(latLng.latitude);
                    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Location")
                            .child("lng").setValue(latLng.longitude);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/m/dd HH:mm:ss");
                    Date date = new Date();

                    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Location")
                            .child("LastOnlineDate").setValue(dateFormat.format(date).toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
