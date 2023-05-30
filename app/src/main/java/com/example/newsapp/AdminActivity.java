package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsapp.data.UsersDao;
import com.example.newsapp.fragment.ManageAdminFragment;
import com.example.newsapp.fragment.ManageCategoryFragment;
import com.example.newsapp.fragment.ManageUserFragment;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private long backPressed;
    private BottomNavigationView bt_nav;
    public static List<Users> usersList;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pre = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean isDark = pre.getBoolean("is_dark", false);
        setTheme(isDark ? R.style.AppThemeDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Lấy User từ SharedPreferences lưu Admin như trong session web
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String jvson = preferences.getString(Constants.ROLE_ADMIN, "");
        Users user = gson.fromJson(jvson, Users.class); // Chuyển đổi chuỗi JSON thành đối tượng User

        //Load data User
        loadUsersData();

        bt_nav = findViewById(R.id.bottom_nav_admin);
        // Thiết lập sự kiện cho BottomNavigationView
        bt_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.manage_user: //quản lý khách hàng
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dataList", (Serializable) usersList);
                        ManageUserFragment manageUserFragment = new ManageUserFragment();
                        manageUserFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, manageUserFragment).commit();
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

    public void loadUsersData() {
        UsersDao usersDao = new UsersDao(getApplicationContext());
        usersDao.getAllUserList(new UsersDao.UserCallback<List<Users>>() {
            @Override
            public void onSuccess(List<Users> data) {
                // Dữ liệu người dùng đã được cập nhật thành công
                // Cập nhật giao diện người dùng tại đây
                usersList = data;

                // Gửi dữ liệu vào fragment Customer
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataList", (Serializable) usersList);
                ManageUserFragment manageUserFragment = new ManageUserFragment();
                manageUserFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, manageUserFragment).commit();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
            }
        });
    }


}