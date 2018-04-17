package com.example.luisguzmn.healthcare40;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("connected",false)){
            Intent intent = new Intent(Splash.this, MainScreen.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
