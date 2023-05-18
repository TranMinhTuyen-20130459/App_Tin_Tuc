package com.example.newsapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.newsapp.fragment.search.HistorySearchFragment;
import com.example.newsapp.fragment.search.ResultSearchFragment;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.SearchNews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public EditText search_news; // thanh tìm kiếm
    public HistorySearchFragment frHistorySearch; // fragment lịch sử tìm kiếm
    public ResultSearchFragment frResultSearch; // fragment kết quả tìm kiếm

    public static ArrayList<ArrayList<News>> list_all_news = new ArrayList<>();

    /**
     * Phương thức này được gọi khi Activity được tạo ra.
     * Nó được sử dụng để khởi tạo các thành phần của Activity như View, Fragment, và các đối tượng liên quan khác.
     * Phương thức này được gọi chỉ một lần trong suốt vòng đời của Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_news = findViewById(R.id.search_news);

        List<String> list_history_search = Arrays.asList("messi", "ronaldo", "neymar");
        createFragmentHistorySearch(list_history_search);

        Bundle bundle = getIntent().getExtras();
        list_all_news = (ArrayList<ArrayList<News>>) bundle.getSerializable("list_all_news");

        // Toast.makeText(this, list_all_news.size() + "", Toast.LENGTH_SHORT).show();
    }

    /**
     * Phương thức này được gọi khi Activity sẵn sàng để tương tác với người dùng.
     * Nó được gọi sau khi Activity đã được khởi tạo và hiển thị trên màn hình.
     * Phương thức này thường được sử dụng để cập nhật dữ liệu của Activity hoặc để đăng ký các listener.
     */

    @Override
    protected void onResume() {
        super.onResume();
        search_news.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                hideFragment(frHistorySearch);

            }
        });

        search_news.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = search_news.getText().toString();
                if (text.isEmpty()) {
                    showFragment(frHistorySearch);
                    hideFragment(frResultSearch);
                } else hideFragment(frHistorySearch);
            }
        });

        search_news.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String keyword = search_news.getText().toString();

                    if (keyword.isEmpty()) {
                        Toast.makeText(getBaseContext(), "Bạn hãy nhập từ khóa để tìm kiếm", Toast.LENGTH_SHORT).show();
                    } else {
                        // xóa đi Fragment
                        removeFragment(frResultSearch);

                        List<News> list_news = SearchNews.searchByKeyword(list_all_news, keyword);

                        if (list_news.size() > 0) {
                            // khởi tạo ra Fragment mới
                            createFragmentResultSearch(list_news);
                        } else {
                            Toast.makeText(getBaseContext(), "Không có kết quả", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }
                return false;
            }

        });

    }

    // tạo fragment HistorySearch
    public void createFragmentHistorySearch(List<String> list_data) {

        // Khởi tạo Fragment
        frHistorySearch = new HistorySearchFragment(list_data);

        // Sử dụng FragmentManager để thêm Fragment vào trong Activity
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_history_search, frHistorySearch)
                .commit();

        // Toast.makeText(this, "Đây là hàm createFragmentHistorySearch()", Toast.LENGTH_SHORT).show();

    }

    // tạo fragment ResultSearch
    public void createFragmentResultSearch(List<News> list_data_news) {

        // khởi tạo Fragment
        frResultSearch = new ResultSearchFragment(list_data_news);

        // sử dụng FragmentManager để thêm Fragment vào trong Activity
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_result_search, frResultSearch)
                .commit();

        // Toast.makeText(this, "Đây là hàm createFragmentResultSearch()", Toast.LENGTH_SHORT).show();

    }

    // ẩn đi một fragment bất kì
    public void hideFragment(Fragment hideFragment) {

        if (hideFragment != null) {
            // Sử dụng FragmentManager để ẩn Fragment vào trong Activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(hideFragment)
                    .commit();
            //  Toast.makeText(this, "Đây là hàm hideFragment()", Toast.LENGTH_SHORT).show();
        }
    }

    // show ra một fragment bất kì
    public void showFragment(Fragment showFragment) {

        if (showFragment != null) {
            // Sử dụng FragmentManager để hiển thị Fragment vào trong Activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(showFragment)
                    .commit();

            //   Toast.makeText(this, "Đây là hàm showFragment()", Toast.LENGTH_SHORT).show();
        }
    }

    // xóa đi một fragment bất kì
    public void removeFragment(Fragment removeFragment) {

        if (removeFragment != null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(removeFragment)
                    .commit();

            // Toast.makeText(this, "Đây là hàm removeFragment()", Toast.LENGTH_SHORT).show();

        }
    }
}