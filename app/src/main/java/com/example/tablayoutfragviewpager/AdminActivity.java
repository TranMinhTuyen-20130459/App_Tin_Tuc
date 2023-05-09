package com.example.tablayoutfragviewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tablayoutfragviewpager.fragment.MainFragment;
import com.example.tablayoutfragviewpager.fragment.ManageAdminFragment;
import com.example.tablayoutfragviewpager.fragment.ManageCategoryFragment;
import com.example.tablayoutfragviewpager.fragment.ManageCustomerFragment;
import com.example.tablayoutfragviewpager.fragment.ProfileFragment;
import com.example.tablayoutfragviewpager.fragment.WidgetFragment;
import com.example.tablayoutfragviewpager.models.Users;
import com.example.tablayoutfragviewpager.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class AdminActivity extends AppCompatActivity {
    private long backPressed;
    private BottomNavigationView bt_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Lấy User từ SharedPreferences lưu Admin như trong session web
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String jvson = preferences.getString(Constants.ROLE_ADMIN, "");
        Users user = gson.fromJson(jvson, Users.class); // Chuyển đổi chuỗi JSON thành đối tượng User

        // mặc định chạy vào fragment quản lý khách hàng
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageCustomerFragment()).commit();
        }
        bt_nav = findViewById(R.id.bottom_nav_admin);
        bt_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manage_user: //quản lý khách hàng
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageCustomerFragment()).commit();
                        bt_nav.getMenu().findItem(R.id.manage_user).setChecked(true);
                        break;

                    case R.id.manage_cate: //quản lý danh mục
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageCategoryFragment()).commit();
                        bt_nav.getMenu().findItem(R.id.manage_cate).setChecked(true);
                        break;

                    case R.id.manage_admin: //quản lý admin
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ManageAdminFragment()).commit();
                        bt_nav.getMenu().findItem(R.id.manage_admin).setChecked(true);
                        break;
                    case R.id.logout_admin:// Đăng xuất
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove(Constants.ROLE_ADMIN);
                        editor.apply();
                        // Đăng xuất và quay về màn hình đăng nhập
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Đóng activity hiện tại
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - backPressed < 2000) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát", Toast.LENGTH_SHORT).show();
            backPressed = currentTime;
        }
    }
}