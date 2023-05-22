package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.newsapp.data.NewsDao;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;

import java.util.ArrayList;

public class ViewedNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewed_news);

        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        NewsDao newsDao = new NewsDao(this);
        args.putSerializable(Constants.KEY_LIST_NEWS_MAIN, (ArrayList<News>) newsDao.getNews());
        args.putBoolean(Constants.KEY_VIEWED_NEWS, true);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}