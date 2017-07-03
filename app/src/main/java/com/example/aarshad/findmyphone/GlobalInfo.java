package com.example.aarshad.findmyphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aarshad on 6/30/17.
 */

public class GlobalInfo {

    public static String phoneNumber = "";
    public static Map<String,String> myTrackers = new HashMap<String,String>();
    Context context;
    SharedPreferences sharedPreferences;

    public  GlobalInfo(Context context){
        this.context=context;
        sharedPreferences =context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }

    public static void updatesInfo(String phoneNumber){


        DateFormat dateFormat = new SimpleDateFormat("yyyy/m/dd HH:mm:ss");
        Date date = new Date();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").child(phoneNumber)
                .child("Updates")
                .setValue(dateFormat.format(date).toString());

    }

    public static String formatPhoneNumber(String Oldnmber){
        try{
            String numberOnly= Oldnmber.replaceAll("[^0-9]", "");
            if(Oldnmber.charAt(0)=='+') numberOnly="+" +numberOnly ;
            if (numberOnly.length()>=10)
                numberOnly=numberOnly.substring(numberOnly.length()-10,numberOnly.length());
            return(numberOnly);
        }
        catch (Exception ex){
            return(" ");
        }
    }


    void saveData() {
        String myTrackersList="" ;
        for (Map.Entry  m:GlobalInfo.myTrackers.entrySet()){
            if (myTrackersList.length()==0)
                myTrackersList=m.getKey() + "%" + m.getValue();
            else
                myTrackersList =myTrackersList+ "%" + m.getKey() + "%" + m.getValue();
        }

        if (myTrackersList.length()==0)
            myTrackersList="empty";

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("MyTrackers",myTrackersList);
        editor.putString("PhoneNumber",phoneNumber);
        editor.commit();
    }

    void loadData(){
        myTrackers.clear();
        phoneNumber= sharedPreferences.getString("PhoneNumber","empty");
        String MyTrackersList= sharedPreferences.getString("MyTrackers","empty");
        if (!MyTrackersList.equals("empty")){
            String[] users=MyTrackersList.split("%");
            for (int i=0;i<users.length;i=i+2){
                myTrackers.put(users[i],users[i+1]);
            }
        }

        if (phoneNumber.equals("empty")){
            // since we have to start Login Activity from normal class so we need Context
            Intent intent=new Intent(context, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // added this to make the flow better. 
            Intent intent=new Intent(context, MyTrackers.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }


}
