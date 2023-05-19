package com.example.newsapp.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.models.Categories;

import java.util.ArrayList;

public class CategoryListFragment extends ListFragment {
    ListView lvCategory;
    ArrayList<Categories> listCategory;
//    String[] arrayCity = {"Hải Phòng", "Nha Trang", "Kháng Hòa", "Huế", "Sài Gòn"};
//    ArrayAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayCity);
//        setListAdapter(adapter);
        View view = inflater.inflate(R.layout.row_category, container, false);
        lvCategory = (ListView) inflater.inflate(R.layout.fragment_category_list, container, false);
        listCategory = new ArrayList<Categories>();
        listCategory.add(new Categories("1", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("2", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("3", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("4", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));

        CategoryAdapter adapter = new CategoryAdapter(
                getActivity(),
                R.layout.row_category,
                listCategory
        );

        lvCategory.setAdapter(adapter);

        return lvCategory;
    }

}