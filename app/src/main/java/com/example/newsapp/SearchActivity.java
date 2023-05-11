package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.newsapp.fragment.search.HistorySearchFragment;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo Fragment
        HistorySearchFragment frHistorySearch = new HistorySearchFragment();

        // Thêm Fragment vào Activity
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_history_search, frHistorySearch)
                .commit();
    }


}