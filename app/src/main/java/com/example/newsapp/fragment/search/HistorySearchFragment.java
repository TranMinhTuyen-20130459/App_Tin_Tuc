package com.example.newsapp.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.adapter.HistorySearchAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistorySearchFragment extends Fragment {

    ListView list_view_search; // ListView lịch sử tìm kiếm bài viết
    List<String> list_data_search = new ArrayList<>();

    public HistorySearchFragment(List<String> list_data_search) {
        this.list_data_search = list_data_search;
    }

    // Tạo view cho Fragment và cấu hình các thành phần trong view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_history_search_fragment, container, false);
        list_view_search = view.findViewById(R.id.list_view_history_search);
        HistorySearchAdapter history_search_adapter = new HistorySearchAdapter(getContext(), android.R.layout.simple_list_item_1, list_data_search);
        // đổ dữ liệu từ adapter về listview
        list_view_search.setAdapter(history_search_adapter);
        return view;
    }

}
