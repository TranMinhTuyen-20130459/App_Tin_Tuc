package com.example.newsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.SearchActivity;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.adapter.ViewPagerAdapter;
import com.example.newsapp.data.CategoriesDao;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// fragment cha chứa các fragment con (các danh mục)
public class MainFragment extends Fragment {
    private ListView mListView;
    private ArrayList<ArrayList<News>> mDataList;
    private ArrayList<News> list;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText searchEditText;
    static int numberOfTitlesLoaded = 0;
    TextView tv_search;
    CategoriesDao categoriesDao = new CategoriesDao();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        tv_search = view.findViewById(R.id.search_text_view);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("list_all_news", (Serializable) MainActivity.list_all_news); // => chuyển danh sách tất cả tin tức qua SearchActivity
                Intent intent_activity_search = new Intent(getContext(), SearchActivity.class);
                intent_activity_search.putExtras(bundle);
                startActivity(intent_activity_search);
            }
        });


        // Khởi tạo đối tượng ViewPager và TabLayout
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDataList = (ArrayList<ArrayList<News>>) bundle.getSerializable(Constants.LIST_TOTAL_CATE);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mDataList);
        // đọc từ database
        categoriesDao.getAllCategoriesList(new CategoriesDao.CategoriesCallback<List<Categories>>(){
            @Override
            public void onSuccess(List<Categories> data){
                for (Categories category : data) {
                    if(category.getActive().equals("1")) adapter.addFragment(new HomeFragment(), category.getTitle());
                }

                // Kết nối TabLayout với ViewPager
                tabLayout.setupWithViewPager(viewPager);
                // Khởi tạo adapter cho ViewPager
                viewPager.setAdapter(adapter);
            }
            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                System.out.println("Error: " + e.toString());
            }
        });
        return view;
    }

    private void arriveFragment(Fragment fr, String keyPackgeBundle, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyPackgeBundle, mDataList.get(position));
        fr.setArguments(bundle);
//        getChildFragmentManager().beginTransaction().replace(R.id.fragment_container_child, fr).commit();
    }

}