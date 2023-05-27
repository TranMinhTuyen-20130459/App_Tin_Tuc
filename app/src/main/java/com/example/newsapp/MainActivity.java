package com.example.newsapp;

import static com.example.newsapp.NewsApp.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.newsapp.data.CategoriesDao;
import com.example.newsapp.fragment.MainFragment;
import com.example.newsapp.fragment.ProfileFragment;
import com.example.newsapp.fragment.WidgetFragment;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.example.newsapp.utils.ReadRSS;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;


    int numberOfTitlesLoaded = 0;
    private BottomNavigationView bt_nav;
    @SuppressWarnings("deprecation")
    ProgressDialog progressDialog;
    long lastClickTime = 0;
    private long backPressed;
    private int count;

    ArrayList<ArrayList<News>> listAll;

    public static ArrayList<ArrayList<News>> list_all_news = new ArrayList<>();
    CategoriesDao categoriesDao = new CategoriesDao();

    @SuppressLint("NonConstantResourceId")
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_nav = findViewById(R.id.bottom_nav);
        // load dữ liệu bảng danh mục ngay tại đây

        // kiểm tra có mạng chưa
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        listAll = new ArrayList<>();
        categoriesDao.getAllCategoriesList(new CategoriesDao.CategoriesCallback<List<Categories>>() {
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

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private void pushNotification(@NonNull News news) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL(news.getLinkImage());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    // Tải về hình ảnh và chuyển thành dạng Bitmap.
                    return BitmapFactory.decodeStream(conn.getInputStream());
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                /* Tạo một Intent để khi người dùng nhấn vào notification thì sẽ chuyển đến NewsDetailActivity */
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class)
                        .putExtra(Constants.KEY_NEWS_DETAILS, news.getLink())
                        .putExtra(Constants.KEY_VIEWED_NEWS, news);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this)
                        .addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(news.getCategory())
                        .setContentText(news.getTitle())
                        .setContentIntent(pendingIntent)
                        .setShowWhen(true)
                        .setAutoCancel(true)
                        // Gắn hình ảnh vào Notification
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
//                        .setLargeIcon(bitmap)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                /* Kiểm tra xem người dùng có cấp quyền POST_NOTIFICATION hay chưa.
                 * Nếu chưa thì yêu cầu người dùng cấp quyền.
                 */
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Người dùng chưa cấp quyền.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Yêu cầu người dùng cấp quyền.
                        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
                    }
                } else {
                    // Người dùng đã cấp quyền.
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.notify(0, builder.build());
                }
            }
        }.execute(news.getLinkImage());
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

        /* Sau khi đã tải hết các tin thì tìm kiếm tin mới nhất, sau đó phát thông báo. */
        if (numberOfTitlesLoaded >= 5 && listAll.size() >= 5) {
            News news = findLatestNews();
            pushNotification(news);
        }
    }

    /**
     * Hàm này sẽ trả về tin mới nhất trong {@code listAll} vừa tải xong.
     *
     * @return tin mới nhất.
     */
    private News findLatestNews() {
        News news = null;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date latest = calendar.getTime();

        // Duyệt tất cả tin trong listAll.
        for (int i = 0; i < listAll.size(); i++) {
            for (int j = 0; j < listAll.get(i).size(); j++) {
                News n = listAll.get(i).get(j);
                if (n != null) {
                    try {
                        final Date date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(n.getDate());
                        if (date != null && date.after(latest)) {
                            // Tin hiện tại xuất hiện sau `latest` -> mới hơn.
                            news = n;
                            news.setCategory(getCategory(i));
                            latest = date;
                        }
                    } catch (ParseException e) {
                        return n;
                    }
                }
            }
        }
        return news;
    }

    private String getCategory(int pos) {
        final String[] categories = {"Tin mới nhất", "Thời sự", "Thể thao", "Thế giới", "Giải trí"};
        return categories[pos];
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
                pushNotification(listAll.get(0).get(0));
            } else {
                // Người dùng chưa cấp quyền
                Toast.makeText(this, "Bạn phải cấp quyền mới truy cập được.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}