package com.example.aarshad.findmyphone;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends IntentService {

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
                    Location location  = TrackLocation.location;
                    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Location")
                            .child("lat").setValue(location.getLatitude());
                    mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).child("Location")
                            .child("lng").setValue(location.getLongitude());

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
