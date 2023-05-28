package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.adapter.ViewedNewsAdapter;
import com.example.newsapp.data.NewsDao;
import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ViewedNewsActivity extends AppCompatActivity implements ViewedNewsAdapter.OnNewsClickListener {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ViewedNewsAdapter adapter;
    private List<News> news;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewed_news);
        setUpActionBar();
        init();
        setUpRecyclerView();
        setUpSwipeRefresh();
        loadData();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void init() {
        refreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(adapter = new ViewedNewsAdapter(this, news = new ArrayList<>(), this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setUpSwipeRefresh() {
        refreshLayout.setOnRefreshListener(this::refreshNews);
    }

    private void refreshNews() {
        loadData();
        refreshLayout.setRefreshing(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        final NewsDao dao = new NewsDao(this);
        news.clear();
        news.addAll(dao.getNews(getCurrentUser()));
        adapter.notifyDataSetChanged();
    }

    private Users getCurrentUser() {
        String json = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE).getString(Constants.ROLE_CUSTOMER, "");
        return new Gson().fromJson(json, Users.class);
    }

    @Override
    public void onNewsClicked(int position) {
        if (actionMode != null) {
            handleItemClick(position);
        } else {
            startActivity(new Intent(this, NewsDetailActivity.class)
                    .putExtra(Constants.KEY_NEWS_DETAILS, news.get(position).getLink()));
        }
    }

    @Override
    public boolean onNewsLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(callback);
        }
        handleItemClick(position);
        return true;
    }

    private void handleItemClick(int position) {
        News news = this.news.get(position);
        news.setSelected(!news.isSelected());
        int count = (int) this.news.stream().filter(News::isSelected).count();
        if (count > 0) {
            actionMode.setTitle("Đã chọn " + count + " news");
        } else {
            actionMode.finish();
        }
        adapter.notifyItemChanged(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteNews() {
        AtomicBoolean undo = new AtomicBoolean(false);
        final List<News> oldNews = new ArrayList<>(this.news);
        final List<News> selectedNews = this.news.stream().filter(News::isSelected).collect(Collectors.toList());
        this.news.removeAll(selectedNews);
        adapter.notifyDataSetChanged();
        Snackbar.make(refreshLayout, "Deletes " + selectedNews.size() + " news", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    this.news.clear();
                    this.news.addAll(oldNews);
                    resetData();
                    undo.set(true);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (!undo.get()) {
                            NewsDao dao = new NewsDao(ViewedNewsActivity.this);
                            if (dao.removeNews(extractLinks(selectedNews))) {
                                Toast.makeText(ViewedNewsActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .show();
        actionMode.finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetData() {
        this.news.forEach(news -> news.setSelected(false));
        adapter.notifyDataSetChanged();
    }

    private String[] extractLinks(List<News> news) {
        String[] result = new String[news.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = news.get(i).getLink();
        }
        return result;
    }

    private final ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.viewed_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                deleteNews();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            resetData();
        }
    };
}