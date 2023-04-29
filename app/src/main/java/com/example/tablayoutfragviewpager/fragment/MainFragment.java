package com.example.tablayoutfragviewpager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tablayoutfragviewpager.News;
import com.example.tablayoutfragviewpager.R;
import com.example.tablayoutfragviewpager.adapter.DataListAdapter;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    ArrayList<News> mDataList;
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mDataList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<News>) bundle.getSerializable("main");
        }
        mListView = view.findViewById(R.id.list_view_home);
        DataListAdapter adapter = new DataListAdapter(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<News> dataList = (ArrayList<News>) bundle.getSerializable("main");
            if (dataList != null) {
                DataListAdapter adapter = new DataListAdapter(getContext(),android.R.layout.simple_list_item_1, dataList);
                mListView.setAdapter(adapter);
            }
        }
    }
}
