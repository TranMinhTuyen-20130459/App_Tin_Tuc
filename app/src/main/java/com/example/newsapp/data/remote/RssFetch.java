package com.example.newsapp.data.remote;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RssFetch {
    private final RssService rssService;

    public RssFetch() {
        rssService = new Retrofit.Builder()
                .baseUrl("https://thanhnien.vn/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(RssService.class);
    }

    public void fetchRss(Callback<String> callback) {
        rssService.getRss("rss/home.rss").enqueue(callback);
    }
}
