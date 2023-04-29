package com.example.tablayoutfragviewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tablayoutfragviewpager.fragment.HomeFragment;
import com.example.tablayoutfragviewpager.fragment.ProfileFragment;
import com.example.tablayoutfragviewpager.fragment.WidgetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int numberOfTitlesLoaded = 0;
    private TabLayout tabLayout;

    private BottomNavigationView bt_nav;
    ArrayList<News> listTile;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_nav = findViewById(R.id.bottom_nav);
        listTile = new ArrayList<>();
        new ReadRSS(MainActivity.this).execute("https://vnexpress.net/rss/suc-khoe.rss");
        new ReadRSS(MainActivity.this).execute("https://vnexpress.net/rss/the-thao.rss");
        bt_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home: // trang chủ
                        if (!listTile.isEmpty()) { // Kiểm tra danh sách có phần tử nào hay chưa
//
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dataList", listTile);
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                            // set checked cho menu item Home
                            bt_nav.getMenu().findItem(R.id.action_home).setChecked(true);
//
                        } else { // Nếu chưa có dữ liệu RSS
                            progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.setMessage("Đang tải dữ liệu, vui lòng đợi...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Đang tải dữ liệu, vui lòng đợi...", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss(); // Ẩn Dialog
                                }
                            }, 500); // Giả lập thời gian tải dữ liệu là 0.5 giây
                        }
                        break;

                    case R.id.action_profile: // trang cá nhân
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                        bt_nav.getMenu().findItem(R.id.action_profile).setChecked(true);
                        break;

                    case R.id.action_widget: // trang tiện ích
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WidgetFragment()).commit();
                        bt_nav.getMenu().findItem(R.id.action_profile).setChecked(true);
                        break;
                }
                return true;
            }
        });
    }

    // dùng để lưu danh sách từ bên ReadRSS
    void onRssRead(ArrayList<News> list) {
        // Lưu danh sách tiêu đề vào biến thành viên
        numberOfTitlesLoaded++;
        listTile = list;
        // Nếu đã có dữ liệu RSS, chuyển sang màn hình Home
        /*
        * Trang HomFragment là cha của Fragment danh mục như: thể thao (SportFragment) ,đời sống, ..(ShoppingFragment)
        *
        * */
        if (numberOfTitlesLoaded > 0 && !listTile.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataList", listTile);
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }

    }
}