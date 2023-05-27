package com.example.newsapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapp.data.CategoriesDao;
import com.example.newsapp.fragment.MainFragment;
import com.example.newsapp.fragment.ProfileFragment;
import com.example.newsapp.fragment.WidgetFragment;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.example.newsapp.utils.ReadRSS;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {
    int numberOfTitlesLoaded = 0;
    private BottomNavigationView bt_nav;

    ProgressDialog progressDialog;
    private long backPressed;

    ArrayList<ArrayList<News>> listAll;

    public static ArrayList<ArrayList<News>> list_all_news = new ArrayList<>();
    CategoriesDao categoriesDao = new CategoriesDao();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_nav = findViewById(R.id.bottom_nav);

        // load dữ liệu bảng danh mục ngay tại đây
        listAll = new ArrayList<>();
        categoriesDao.getAllCategoriesList(new CategoriesDao.CategoriesCallback<>() {
            @Override
            public void onSuccess(List<Categories> data) {
                ArrayList<String> rssUrls = new ArrayList<>();
                for (Categories c : data) {
                    if (c.getActive().equals("1")) rssUrls.add(c.getUrl());
                }

                // kiểm tra có mạng chưa
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (!isConnected) { // nếu chưa có mạng
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                            .setTitle("Không có kết nối internet");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // nếu có mạng đọc link RSS
                    for (String url : rssUrls) {
                        ReadRSS readRSS = new ReadRSS(MainActivity.this);
                        readRSS.execute(url);
                        listAll.add(readRSS.getListNews());
                    }
                    list_all_news = listAll;
                }

                bt_nav.setOnNavigationItemSelectedListener(item -> {
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
                            } else { // Nếu chưa có dữ liệu RSS
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Đang tải dữ liệu, vui lòng đợi...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                new Handler().postDelayed(() -> {
                                    MainActivity.this.recreate(); // reload lại Activity
                                    progressDialog.dismiss(); // Ẩn Dialog
                                }, 500); // Giả lập thời gian tải dữ liệu là 0.5 giây
                            }
                            break;

                        case R.id.action_profile: // trang cá nhân
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                            bt_nav.getMenu().findItem(R.id.action_profile).setChecked(true);
                            break;

                        case R.id.action_widget: // trang tiện ích
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WidgetFragment()).commit();
                            bt_nav.getMenu().findItem(R.id.action_widget).setChecked(true);
                            break;
                    }
                    return true;
                });
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                System.out.println("Error: " + e.toString());
            }
        });
    }

    // dùng để lưu danh sách từ bên ReadRSS
    public void onRssRead() {
        numberOfTitlesLoaded++;
        // Nếu đã có dữ liệu RSS, chuyển sang màn hình Home
        /*
         * Trang MainFragment là cha của Fragment danh mục
         * */
        // vận chuyển dữ liệu lên Mainfragment khi chưa dùng view pager( lúc mở ứng dụng)
        if (numberOfTitlesLoaded > 0 && !listAll.isEmpty()) {
            Log.d("TAG", "onRssRead: ");
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.LIST_TOTAL_CATE, listAll);
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment).commitAllowingStateLoss();
        }
    }

    // nhấn 2 lần thể thoát
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - backPressed < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT).show();
            backPressed = currentTime;
        }
    }
}