package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.newsapp.data.NewsDao;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewedNewsActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewed_news);
        setUpFragment();
        setUpRefresh();
    }

    private void setUpFragment() {
        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        NewsDao newsDao = new NewsDao(this);
        args.putSerializable(Constants.KEY_LIST_NEWS_MAIN, (ArrayList<News>) newsDao.getNews(getCurrentUser()));
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void setUpRefresh() {
        refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(this::refreshNews);
    }

    private void refreshNews() {
        setUpFragment();
        refreshLayout.setRefreshing(false);
    }

    private Users getCurrentUser() {
        String json = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE).getString(Constants.ROLE_CUSTOMER, "");
        return new Gson().fromJson(json, Users.class);
    }
}