package com.example.aarshad.findmyphone;

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


}
