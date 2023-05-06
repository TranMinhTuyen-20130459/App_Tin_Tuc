package com.example.tablayoutfragviewpager.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tablayoutfragviewpager.NewsDetailActivity;
import com.example.tablayoutfragviewpager.models.News;

import java.util.ArrayList;

public class PutLinkToNewDetail {
    private Context context;

    public PutLinkToNewDetail(Context context) {
        this.context = context;
    }
    public void putLinkNews(ListView listView, ArrayList<News> mDataList){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("link",mDataList.get(i).getLink());
                context.startActivity(intent);
            }
        });
    }
}
