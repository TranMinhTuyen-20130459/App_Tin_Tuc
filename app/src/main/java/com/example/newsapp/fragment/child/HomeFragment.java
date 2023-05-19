package com.example.newsapp.fragment.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.adapter.DataListAdapter;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.example.newsapp.utils.PutLinkToNewsDetail;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<News> mDataList;
    ListView mListView;

    /* Nếu là true thì fragment này đang hiển thị danh sách tin đã xem. */
    private boolean isViewed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        mDataList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<News>) bundle.getSerializable(Constants.KEY_LIST_NEWS_MAIN);
            isViewed = bundle.getBoolean(Constants.KEY_VIEWED_NEWS);
        }

        mListView = view.findViewById(R.id.list_view_home);
        DataListAdapter adapter = new DataListAdapter(getContext(), android.R.layout.simple_list_item_1, mDataList, isViewed, mListView);
        mListView.setAdapter(adapter);
        // chi tiết từng bài viết
        if (!isViewed) {
            new PutLinkToNewsDetail(getActivity()).putLinkNews(mListView,mDataList);
        }
        return view;
    }

}