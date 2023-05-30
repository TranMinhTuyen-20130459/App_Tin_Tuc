package com.example.newsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapp.data.NewsDao;
import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.gson.Gson;

public class NewsDetailActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        webView = findViewById(R.id.content_news);
        Intent intent = getIntent();
        String link = intent.getStringExtra(Constants.KEY_NEWS_DETAILS);
        if (link != null) {
            dialog = new ProgressDialog(NewsDetailActivity.this);
            dialog.setMessage("Đang tải...");
            dialog.setCancelable(false);
            dialog.show();
            // Tăng tốc WebView
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            // dùng để khi nhấn vào những cái link báo khác thì nó vẫn ở trong app chứ nhảy ra khỏi app
            webView.setWebViewClient(onWebViewLoaded);
            webView.loadUrl(link);
        }

        String json = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE).getString(Constants.ROLE_CUSTOMER, "");
        Users user = new Gson().fromJson(json, Users.class);
        if (user != null) {
            saveNews(user);
        }
    }

    private final WebViewClient onWebViewLoaded = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            AlertDialog.Builder builder = new AlertDialog.Builder(NewsDetailActivity.this);
            builder.setMessage("Bạn cần kết nối internet để sử dụng ứng dụng này")
                    .setTitle("Không có kết nối internet");
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void saveNews(Users user) {
        News news = (News) getIntent().getSerializableExtra(Constants.KEY_VIEWED_NEWS);
        if (news != null) {
            new Thread(() -> {
                NewsDao newsDao = new NewsDao(this);
                newsDao.addNews(news, user.getUsername());
            }).start();
        }
    }
}