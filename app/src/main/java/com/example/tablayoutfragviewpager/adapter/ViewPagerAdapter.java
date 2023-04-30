package com.example.tablayoutfragviewpager.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.tablayoutfragviewpager.models.News;
import com.example.tablayoutfragviewpager.fragment.child.EntertainmentFragment;
import com.example.tablayoutfragviewpager.fragment.child.HomeFragment;
import com.example.tablayoutfragviewpager.fragment.child.PolicalFragment;
import com.example.tablayoutfragviewpager.fragment.child.ShoppingFragment;
import com.example.tablayoutfragviewpager.fragment.child.SportFragment;

import java.util.ArrayList;

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
//        switch (position) {
//            case 0:
//                HomeFragment mainFragment = new HomeFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("main", dataList);
//                mainFragment.setArguments(bundle);
//                return mainFragment;
//            case 1:
//                return new PolicalFragment();
//            case 2:
//                return new SportFragment();
//            case 3:
//                return new ShoppingFragment();
//            case 4:
//                return new EntertainmentFragment();
//            default:
//                return new HomeFragment();
//        }
        if (position == 0) {
            HomeFragment mainFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("main", dataList.get(position));
            mainFragment.setArguments(bundle);
            return mainFragment;

        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", dataList.get(position));
            mFragments.get(position).setArguments(bundle);
            return mFragments.get(position);
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
    // số danh mục

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        String title = "";
//        // các tên thể loại tương ứng với các lớp ở trên
//        switch (position) {
//            case 0:
//                title = "Trang chủ";
//                break;
//            case 1:
//                title = "Thời sự";
//                break;
//            case 2:
//                title = "Thể Thao";
//                break;
//            case 3:
//                title = "Đời sống";
//                break;
//            case 4:
//                title = "Giải trí";
//                break;
//            default:
//                title = "Đời sống";
//        }
        return mFragmentTitles.get(position);
    }
}
