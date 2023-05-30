package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_TIMEOUT = 2000; // Thời gian hiển thị Splash Screen (2 giây)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent, ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this).toBundle());
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}