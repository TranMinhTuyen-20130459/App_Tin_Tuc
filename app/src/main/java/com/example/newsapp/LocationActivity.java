package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.newsapp.fragment.MapFragment;



public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tri);

        // google map
        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

        //google map

    }
}