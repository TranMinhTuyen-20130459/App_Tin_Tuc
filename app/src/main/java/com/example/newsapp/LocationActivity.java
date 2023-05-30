package com.example.newsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.newsapp.fragment.MapFragment;



public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pre = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean isDark = pre.getBoolean("is_dark", false);
        setTheme(isDark ? R.style.AppThemeDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tri);

        // google map
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

        //google map

    }
}