package com.pedroaguilar.andarivel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        long time = 3000L;
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        };
        handler.postDelayed(r, time);
    }
}
