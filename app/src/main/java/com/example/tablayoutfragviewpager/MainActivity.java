package com.example.tablayoutfragviewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tablayoutfragviewpager.fragment.MainFragment;
import com.example.tablayoutfragviewpager.fragment.ProfileFragment;
import com.example.tablayoutfragviewpager.fragment.WidgetFragment;
import com.example.tablayoutfragviewpager.models.News;
import com.example.tablayoutfragviewpager.utils.ReadRSS;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int numberOfTitlesLoaded = 0;
    private BottomNavigationView bt_nav;
    ProgressDialog progressDialog;

    ArrayList<ArrayList<News>> listAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_nav = findViewById(R.id.bottom_nav);
//        listTile = new ArrayList<>();
        listAll = new ArrayList<>();
        String[] rssUrls = {
                "https://vnexpress.net/rss/tin-moi-nhat.rss",
                "https://vnexpress.net/rss/thoi-su.rss",
                "https://vnexpress.net/rss/the-thao.rss",
                "https://vnexpress.net/rss/the-gioi.rss",
                "https://vnexpress.net/rss/giai-tri.rss"
        };
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                    .setTitle("Không có kết nối internet");
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // đọc link RSS
            for (String url : rssUrls) {
                ReadRSS readRSS = new ReadRSS(MainActivity.this);
                readRSS.execute(url);
                listAll.add(readRSS.getListNews());
            }
            bt_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_home: // trang chủ
                            if (!listAll.isEmpty()) { // Kiểm tra danh sách có phần tử nào hay chưa
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("dataList", listAll);
                                MainFragment mainFragment = new MainFragment();
                                mainFragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
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
    }

    // dùng để lưu danh sách từ bên ReadRSS
    public void onRssRead() {
        numberOfTitlesLoaded++;
        // Nếu đã có dữ liệu RSS, chuyển sang màn hình Home
        /*
         * Trang MainFragment là cha của Fragment danh mục như: thể thao (SportFragment) ,đời sống, ..(ShoppingFragment)
         *
         * */
        if (numberOfTitlesLoaded > 0 && !listAll.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataList", listAll);
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commit();
        }

    }
}