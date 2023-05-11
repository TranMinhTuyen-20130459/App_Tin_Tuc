package com.example.newsapp.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.adapter.HistorySearchAdapter;

import java.util.Arrays;
import java.util.List;

public class HistorySearchFragment extends Fragment {

    ListView list_view_search; // ListView lịch sử tìm kiếm bài viết
    List<String> list_data_search = Arrays.asList("messi", "ronaldo", "neymar");

    //List<String> list_data_search = Arrays.asList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tạo và trả về view cho Fragment Lịch sử tìm kiếm
        View view = inflater.inflate(R.layout.list_history_search_fragment, container, false);
        list_view_search = view.findViewById(R.id.list_view_history_search);
        HistorySearchAdapter history_search_adapter = new HistorySearchAdapter(getContext(), android.R.layout.simple_list_item_1, list_data_search);
        list_view_search.setAdapter(history_search_adapter);
        return view;
    }

}
