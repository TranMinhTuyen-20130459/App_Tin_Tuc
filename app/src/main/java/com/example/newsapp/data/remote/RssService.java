package com.example.newsapp.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssService {

    @GET
    Call<String> getRss(@Url String url);
}
