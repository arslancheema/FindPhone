package com.example.aarshad.findmyphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText editTextNumber ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNumber = (EditText) findViewById(R.id.EDTNumber);

    }

    public void buttonNextClick(View view){
        GlobalInfo.phoneNumber = GlobalInfo.formatPhoneNumber(editTextNumber.getText().toString());
        GlobalInfo.updatesInfo(GlobalInfo.phoneNumber);
        finish();
        Intent i = new Intent(this, MyTrackers.class);
        startActivity(i);
    }
}
