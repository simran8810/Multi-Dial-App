package com.example.multi_dial_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class SplashScreenActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen_layout);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_info", 0);

        Boolean isLoggedIn = preferences.getBoolean(getString(R.string.isLoggedIn), false);


        if(isLoggedIn) {
            //checking if user is already logged in
            Intent intent = new Intent(getApplicationContext(), CallActivity.class);

            startActivity(intent);

            return;
        }
        else{                         //if not we send him to candidate/recruiter screen

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);

            finish();

            return;
        }


    }





}
