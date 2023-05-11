package com.example.newsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.fragment.child.EntertainmentFragment;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.fragment.child.PolicalFragment;
import com.example.newsapp.fragment.child.ShoppingFragment;
import com.example.newsapp.fragment.child.SportFragment;
import com.example.newsapp.models.News;
import com.example.newsapp.R;
import com.example.newsapp.adapter.ViewPagerAdapter;
import com.example.newsapp.utils.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

// fragment cha chứa các fragment con (các danh mục)
public class MainFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ArrayList<News>> mDataList;
    private ArrayList<News> list;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText searchEditText;
    static int numberOfTitlesLoaded = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        // Khởi tạo đối tượng ViewPager và TabLayout
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<ArrayList<News>>) bundle.getSerializable(Constants.LIST_TOTAL_CATE);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mDataList);
        // đọc từ database
        adapter.addFragment(new HomeFragment(), "Trang chủ");
        adapter.addFragment(new PolicalFragment(), "Thời sự");
        adapter.addFragment(new SportFragment(), "Thể thao");
        adapter.addFragment(new ShoppingFragment(), "Đời sống");
        adapter.addFragment(new EntertainmentFragment(), "Giải trí");
        adapter.addFragment(new EntertainmentFragment(), "Sức khỏe");
        adapter.addFragment(new EntertainmentFragment(), "Quân sự");
        adapter.addFragment(new EntertainmentFragment(), "Giáo dục");
        adapter.addFragment(new EntertainmentFragment(), "Du lịch");
//        viewPager.setAdapter(adapter);



        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
        // Khởi tạo adapter cho ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // Xử lý khi người dùng chọn tab
//                switch (position) {
//                    case 0:
//                        // Mở HomeFragment (danh mục Trang chủ)
//                        HomeFragment homeFragment = new HomeFragment();
//                        arriveFragment(homeFragment, "main", position);
//                        break;
//                    case 1: // danh mục thời sự
//                        PolicalFragment profileFragment = new PolicalFragment();
//                        arriveFragment(profileFragment, "data", position);
//                        break;
//                    case 2:
//                        // Mở SportFragment (danh mục thể thao)
//                        SportFragment sportFragment = new SportFragment();
//                        arriveFragment(sportFragment, "data", position);
//                        break;
//                    case 3:
//                        ShoppingFragment shoppingFragment = new ShoppingFragment();
//                        arriveFragment(shoppingFragment, "data", position);
//                        break;
//                    case 4:
//                        EntertainmentFragment entertainmentFragment = new EntertainmentFragment();
//                        arriveFragment(entertainmentFragment, "data", position);
//                        break;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(adapter);


//        setUserVisibleHint(true);
        return view;
    }

    private void arriveFragment(Fragment fr, String keyPackgeBundle, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyPackgeBundle, mDataList.get(position));
        fr.setArguments(bundle);
//        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, fr).commit();
    }

}
