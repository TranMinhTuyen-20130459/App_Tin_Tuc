package com.example.newsapp.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.newsapp.models.News;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.utils.Constants;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<ArrayList<News>> dataList;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mFragmentTitles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<ArrayList<News>> dataList) {
        super(fm, behavior);
        this.dataList = dataList;
    }


    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    // sử dụng Viewpager
    @NonNull
    @Override
    public Fragment getItem(int position) {
//       danh sách bài viết trong danh mục
            HomeFragment mainFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_LIST_NEWS_MAIN, dataList.get(position).stream()
                    .limit(Constants.QUANTITY_NEWS_OF_CATE)
                    .collect(Collectors.toCollection(ArrayList::new)));
            mainFragment.setArguments(bundle);
            return mainFragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
    // số danh mục

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//       tên danh mục
        return mFragmentTitles.get(position);
    }
}
