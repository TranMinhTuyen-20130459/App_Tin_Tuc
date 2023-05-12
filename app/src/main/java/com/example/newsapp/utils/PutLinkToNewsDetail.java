package com.example.newsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.models.News;

import java.util.ArrayList;

public class PutLinkToNewsDetail {
    private final Context context;

    public PutLinkToNewsDetail(Context context) {
        this.context = context;
    }

    public void putLinkNews(ListView listView, ArrayList<News> mDataList) {
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(Constants.KEY_NEWS_DETAILS, mDataList.get(i).getLink());
            intent.putExtra(Constants.KEY_VIEWED_NEWS, mDataList.get(i));
            context.startActivity(intent);
        });
    }
}