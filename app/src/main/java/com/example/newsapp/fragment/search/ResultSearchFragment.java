package com.example.newsapp.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.adapter.ResultSearchAdapter;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.PutLinkToNewsDetail;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchFragment extends Fragment {

    private ListView list_view_result_search;
    private List<News> list_data_result_search;

    public ResultSearchFragment(List<News> list_data_result_search) {
        this.list_data_result_search = list_data_result_search;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_result_search_fragment, container, false);
        list_view_result_search = view.findViewById(R.id.list_view_result_search);
        ResultSearchAdapter result_search_adapter = new ResultSearchAdapter(getContext(), android.R.layout.simple_list_item_1, list_data_result_search);
        // đổ dữ liệu từ ResultSearchAdapter về ListView
        list_view_result_search.setAdapter(result_search_adapter);
        new PutLinkToNewsDetail(getActivity()).putLinkNews(list_view_result_search,(ArrayList<News>) list_data_result_search);
        return view;
    }
}
