package com.example.newsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Lớp này dùng để tạo đối tượng SQLiteDatabase cho việc đọc/ghi dữ liệu.
 */
public class NewsDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "news.db";
    private static final int DB_VERSION = 1;

    /**
     * Câu truy vấn để tạo bảng viewed_news.
     * | column   | data type|
     * |----------|----------|
     * | link     |   TEXT   |
     * | title    |   TEXT   |
     * | img_link |   TEXT   |
     * | data     |   DATE   |
     * | user     |   TEXT   |
     */
    private static final String SQL_CREATE_VIEWED_NEWS =
            "CREATE TABLE viewed_news (" +
                    "link TEXT PRIMARY KEY," +
                    "title TEXT," +
                    "img_link TEXT," +
                    "date DATE," +
                    "user TEXT)";

    /**
     * Câu truy vấn để xóa bảng viewed_news.
     */
    private static final String SQL_DELETE_VIEWED_NEWS = "DROP TABLE IF EXISTS viewed_news";

    public NewsDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng viewed_news.
        db.execSQL(SQL_CREATE_VIEWED_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng viewed_news.
        db.execSQL(SQL_DELETE_VIEWED_NEWS);
        // Gọi lại phương thức onCreate() -> tạo lại bảng viewed_news.
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
