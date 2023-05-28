package com.example.newsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.newsapp.data.remote.RssFetch;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.ViewedNewsNotification;
import com.example.newsapp.utils.XMLDOMParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewedNewsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ViewedNewsNotification newsNotification = new ViewedNewsNotification(context);
        if (!newsNotification.isFull()) {
            new RssFetch().fetchRss(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        XMLDOMParser xmldomParser = new XMLDOMParser();
                        News news = xmldomParser.parseFirst(response.body());
                        if (news != null && !newsNotification.isActive(news.getLink().hashCode())) {
                            newsNotification.pushNotification(news);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}