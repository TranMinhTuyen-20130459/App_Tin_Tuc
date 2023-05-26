package com.example.newsapp.utils;

import com.example.newsapp.models.News;

import java.util.ArrayList;
import java.util.List;

public class SearchNews {

    public static List<News> searchByKeyword(ArrayList<ArrayList<News>> list_all_news, String keyword) {
        List<News> result = new ArrayList<>();
        for (ArrayList<News> arrayList : list_all_news) {
            for (News news : arrayList) {

                if (news.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    result.add(news);

                if (news.getDate().toLowerCase().contains(keyword.toLowerCase()))
                    result.add(news);

            }
        }
        return result;
    }
}
