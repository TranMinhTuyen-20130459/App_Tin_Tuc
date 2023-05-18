package com.example.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class ManagerHistorySearch {

    private static final String SHARED_PREFS_NAME = "search_history";
    private static final String KEY_SEARCH_HISTORY = "search_history_set";

    private SharedPreferences sharedPreferences;

    public ManagerHistorySearch(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveSearchQuery(String searchQuery) {

        Set<String> searchHistorySet = getSearchHistory();
        searchHistorySet.add(searchQuery);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_SEARCH_HISTORY, searchHistorySet);
        editor.commit();

    }

    public Set<String> getSearchHistory() {
//        System.out.println("loi tai ham getSearchHistory()");
        return sharedPreferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<>());
    }

    public void clearHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SEARCH_HISTORY);
        editor.commit();
    }

}
