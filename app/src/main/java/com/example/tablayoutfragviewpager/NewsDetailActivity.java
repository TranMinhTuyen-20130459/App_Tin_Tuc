package com.example.tablayoutfragviewpager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsDetailActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        webView = findViewById(R.id.content_news);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        if (link != null) {
            dialog = new ProgressDialog(NewsDetailActivity.this);
            dialog.setMessage("Đang tải...");
            dialog.setCancelable(false);
            dialog.show();
            // dùng để khi nhấn vào những cái link báo khác thì nó vẫn ở trong app chứ nhảy ra khỏi app
            webView.setWebViewClient(onWebViewLoaded);
            webView.loadUrl(link);
        }
    }
    private WebViewClient onWebViewLoaded = new WebViewClient(){
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
}