package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_TIMEOUT = 2000; // Thời gian hiển thị Splash Screen (2 giây)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // kiểm tra có mạng chưa
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (!isConnected) { // nếu chưa có mạng
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
                    builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                            .setTitle("Không có kết nối internet");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}