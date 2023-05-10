package com.example.tablayoutfragviewpager.fragment.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tablayoutfragviewpager.models.News;
import com.example.tablayoutfragviewpager.R;
import com.example.tablayoutfragviewpager.adapter.DataListAdapter;
import com.example.tablayoutfragviewpager.utils.Constants;
import com.example.tablayoutfragviewpager.utils.PutLinkToNewsDetail;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {
    ArrayList<News> mDataList;
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        mDataList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<News>) bundle.getSerializable(Constants.KEY_LIST_NEWS_AUXI);
        }
        mListView = view.findViewById(R.id.list_view_home);
        DataListAdapter adapter = new DataListAdapter(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(adapter);
        // chi tiết từng bài viết
        new PutLinkToNewsDetail(getActivity()).putLinkNews(mListView,mDataList);
        return view;
    }

}