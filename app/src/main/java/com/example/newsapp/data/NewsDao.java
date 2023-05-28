package com.example.newsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class NewsDao {
    private final NewsDbHelper dbHelper;

    public NewsDao(@NonNull Context context) {
        dbHelper = new NewsDbHelper(context);
    }

    /**
     * Lấy tất cả tin người dùng đã xem trong database.
     *
     * @return danh sách các tin.
     */
    public List<News> getNews(Users users) {
        final List<News> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("viewed_news", null, "user=?", new String[]{users.getUsername()},
                null, null, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String link = cursor.getString(cursor.getColumnIndexOrThrow("link"));
            String imgLink = cursor.getString(cursor.getColumnIndexOrThrow("img_link"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            News news = new News(title, link, imgLink, date, description);
            list.add(news);
        }

        cursor.close();
        return list;
    }

    /**
     * Thêm tin đã xem vào database.
     *
     * @param news tin người dùng đã xem.
     * @return {@code true} nếu thêm vào thành công, {@code false} nếu thêm vào thất bại.
     */
    public boolean addNews(News news, String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", news.getTitle());
        values.put("link", news.getLink());
        values.put("img_link", news.getLinkImage());
        values.put("date", news.getDate());
        values.put("description", news.getDescription());
        values.put("user", username);
        return db.insert("viewed_news", null, values) != -1;
    }

    public boolean removeNews(String link) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("viewed_news", "link = ?", new String[]{link}) != 0;
    }

    public boolean removeNews(String... links) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (String link : links) {
                if (!removeNews(link)) {
                    return false;
                }
            }
            db.setTransactionSuccessful();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }
}
