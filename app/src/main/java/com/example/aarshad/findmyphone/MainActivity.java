package com.example.aarshad.findmyphone;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TrackerAdapter trackerAdapter ;
    ArrayList<AdapterItems> listTrackers = new ArrayList<AdapterItems>();
    DatabaseReference mDatabaseReference ;

    @Override
    protected void onResume() {
        super.onResume();
        refreshDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        GlobalInfo globalInfo = new GlobalInfo(this);
        globalInfo.loadData();
        checkLocationPermission();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AdapterItems adapterItems = listTrackers.get(position);
                Intent mapIntent = new Intent(getApplicationContext(),MapsActivity.class);
                mapIntent.putExtra("phone_number",adapterItems.phoneNumber);
                startActivity(mapIntent);
            }
        });
        trackerAdapter = new TrackerAdapter(this, R.layout.single_row_contact, listTrackers);
        listView.setAdapter(trackerAdapter);


    }
    private void refreshDb() {
        listTrackers.clear();


        mDatabaseReference.child("Users").child(GlobalInfo.phoneNumber).
                child("Finders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(getApplicationContext(),"Listned to Finders",Toast.LENGTH_SHORT).show();

                Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();

                listTrackers.clear();
                if (td == null)  //no one allow you to find him
                {
                    listTrackers.add(new AdapterItems("NoTicket", "no_desc"));
                    trackerAdapter.notifyDataSetChanged();
                    return;
                }
                // List<Object> values = td.values();

                // get all contact to list
                ArrayList<AdapterItems> listContact = new ArrayList<AdapterItems>();
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    listContact.add(new AdapterItems(  name,GlobalInfo.formatPhoneNumber(phoneNumber)
                    ));

                }

// if the name is save chane his text
                // case who find me
                String tinfo;
                for (  String Numbers : td.keySet()) {
                    for (AdapterItems cs : listContact) {

                        //IsFound = SettingSaved.WhoIFindIN.get(cs.Detals);  // for case who i could find list
                        if (cs.phoneNumber.length() > 0)
                            if (Numbers.contains(cs.phoneNumber)) {
                                listTrackers.add(new AdapterItems(cs.userName, cs.phoneNumber));
                                break;
                            }
                    }
                }
                trackerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        trackerAdapter.notifyDataSetChanged();
    }

    public void startService(){
        // start location track

        if (!TrackLocation.isRunning){
            TrackLocation trackLocation = new TrackLocation();
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, trackLocation);
        }
        if (!MyService.isRunning){
            Intent i = new Intent(this, MyService.class);
            startService(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addtracker:
                Intent i = new Intent(this, MyTrackers.class);
                startActivity(i);
                return true;
            case R.id.help:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void checkLocationPermission(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }
        startService();

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"Permission Denied" , Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
