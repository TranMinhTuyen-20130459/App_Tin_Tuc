package com.example.newsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.adapter.DataListAdapter;
import com.example.newsapp.models.Categories;
import com.example.newsapp.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageCategoryFragment extends Fragment {
    ListView mListView;
    ArrayList<Categories> listCategory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_category_fragment, container, false);
        listCategory = new ArrayList<>();


        // Tạo danh sách đối tượng Person
        listCategory.add(new Categories("1", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("2", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("3", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("4", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));

        mListView = view.findViewById(R.id.list_view);
        CategoryAdapter adapter = new CategoryAdapter(getContext(), android.R.layout.simple_list_item_1, listCategory);
        mListView.setAdapter(adapter);

        return view;
    }
}