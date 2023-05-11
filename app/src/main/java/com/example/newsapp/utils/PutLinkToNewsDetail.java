package com.example.newsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.models.News;

import java.util.ArrayList;

public class PutLinkToNewsDetail {
    private Context context;

    public PutLinkToNewsDetail(Context context) {
        this.context = context;
    }
    public void putLinkNews(ListView listView, ArrayList<News> mDataList){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(Constants.KEY_NEWS_DETAILS,mDataList.get(i).getLink());
                context.startActivity(intent);
            }
        });
    }
}
