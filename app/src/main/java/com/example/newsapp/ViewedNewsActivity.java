package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.newsapp.data.NewsDao;
import com.example.newsapp.fragment.child.HomeFragment;
import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewedNewsActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewed_news);
        setUpFragment();    // 2
        setUpRefresh();
    }

    /**
     * Lấy toàn bộ tin đã xem của người dùng hiện tại (đang đăng nhập) sau đó truyền qua cho HomeFragment hiển thị.
     */
    private void setUpFragment() {
        // Tạo mới HomeFragment dùng để hiển thị danh sách tin.
        Fragment fragment = new HomeFragment();
        // Tạo đối tượng Bundle dùng để truyền dữ liệu cho HomeFragment khi nó được tạo.
        Bundle args = new Bundle();
        // Tạo đối tượng NewsDao (DAO) dùng để tương tác với database (truy vấn, thêm, sửa, xóa, cập nhật)
        NewsDao newsDao = new NewsDao(this);
        /* Dữ liệu khi truyền tới HomeFragment được lưu dưới dạng key-value.
         * Đầu tiên lấy toàn bộ tin đã xem của người dùng hiện tại, sau đó lưu vào Bundle (biến args ở trên) với key là KEY_LIST_NEWS_MAIN = "main"
         */
        args.putSerializable(Constants.KEY_LIST_NEWS_MAIN, (ArrayList<News>) newsDao.getNews(getCurrentUser()));
        /* Tiếp theo cần cho HomeFragment biết nó đang hiển thị tin đã xem. */
        args.putBoolean(Constants.KEY_VIEWED_NEWS, true);
        // Truyền dữ liệu cho fragment.
        fragment.setArguments(args);

        // Hiển thị fragment ra màn hình.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Cài đặt chức năng kéo từ trên xuống để tải lại dữ liệu.
     */
    private void setUpRefresh() {
        // Ánh xạ View.
        refreshLayout = findViewById(R.id.swipe_refresh);
        // Khi người dùng swipe ngón tay từ trên xuống, hàm refreshNews() sẽ được gọi.
        refreshLayout.setOnRefreshListener(this::refreshNews);
    }

    private void refreshNews() {
        // Lấy dữ liệu từ database và hiển thị lên màn hình.
        setUpFragment();
        // Ẩn reload icon.
        refreshLayout.setRefreshing(false);
    }

    /*
     * Lấy thông tin người dùng đang đăng nhập.
     */
    private Users getCurrentUser() {
        // Thông tin mới lấy ra ở dạng JSON (String).
        String json = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE).getString(Constants.ROLE_CUSTOMER, "");
        // Chuyển đổi JSON thành đối tượng Users rồi trả về.
        return new Gson().fromJson(json, Users.class);
    }
}