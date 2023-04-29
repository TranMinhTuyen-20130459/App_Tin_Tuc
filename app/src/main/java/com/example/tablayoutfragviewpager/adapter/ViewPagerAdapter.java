package com.example.tablayoutfragviewpager.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.tablayoutfragviewpager.News;
import com.example.tablayoutfragviewpager.fragment.HomeFragment;
import com.example.tablayoutfragviewpager.fragment.MainFragment;
import com.example.tablayoutfragviewpager.fragment.ProfileFragment;
import com.example.tablayoutfragviewpager.fragment.SportFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<News> dataList;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<News> dataList) {
        super(fm, behavior);
        this.dataList = dataList;
    }

    // sử dụng Viewpager
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainFragment mainFragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("main", dataList);
                mainFragment.setArguments(bundle);
                return mainFragment;
            case 1:
                return new ProfileFragment();
            case 2:
                return new SportFragment();
            case 3:
                return new SportFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
    // số danh mục

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        // các tên thể loại tương ứng với các lớp ở trên
        switch (position) {
            case 0:
                title = "Trang chủ";
                break;
            case 1:
                title = "Thời sự";
                break;
            case 2:
                title = "Thể Thao";
                break;
            case 3:
                title = "Đời sống";
                break;
            default:title = "Đời sống";
        }
        return title;
    }
}
