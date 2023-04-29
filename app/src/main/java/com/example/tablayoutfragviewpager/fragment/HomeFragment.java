package com.example.tablayoutfragviewpager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tablayoutfragviewpager.MainActivity;
import com.example.tablayoutfragviewpager.News;
import com.example.tablayoutfragviewpager.ReadRSS;
import com.example.tablayoutfragviewpager.adapter.DataListAdapter;
import com.example.tablayoutfragviewpager.R;
import com.example.tablayoutfragviewpager.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
// fragment cha chứa các fragment con (các danh mục)
public class HomeFragment extends Fragment {
    private ListView mListView;
    private ArrayList<News> mDataList;
    private ArrayList<News> list;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText searchEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Khởi tạo đối tượng ViewPager và TabLayout
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<News>) bundle.getSerializable("dataList");
        }


        // Khởi tạo adapter cho ViewPager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mDataList);

        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
        // Khởi tạo adapter cho ViewPager
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Xử lý khi người dùng chọn tab
                switch (position) {
                    case 0:
                        // Mở MainFragment (danh mục Trang chủ)
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("main", mDataList);
                        MainFragment mainFragment = new MainFragment();
                        mainFragment.setArguments(bundle1);
                        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, mainFragment).commit();
                        break;
                    case 1: // danh mục thời sự
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("hello", list);
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle2);
                        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, profileFragment).commit();
                        break;
                    case 2:
                        // Mở SportFragment (danh mục thể thao)
                        SportFragment sportFragment = new SportFragment();
                        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, sportFragment).commit();
                        break;
                        case 3:
                        // đời sống
                        ShoppingFragment shoppingFragment = new ShoppingFragment();
                        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, shoppingFragment).commit();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        setUserVisibleHint(true);
        return view;
    }

}
