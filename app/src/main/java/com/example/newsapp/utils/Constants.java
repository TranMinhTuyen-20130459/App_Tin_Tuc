package com.example.newsapp.utils;

public class Constants {

    private Constants() {
    }

    public static final String MY_PREFERENCES = "MyPreferences";
    public static final String KEY_VIEWED_NEWS = "viewed_news";
    public static final String LIST_TOTAL_CATE = "dataList";
    public static final String KEY_LIST_NEWS_MAIN = "main"; // danh sách bài viết của danh mục chính ( trang chủ)
    public static final String KEY_LIST_NEWS_AUXI = "data"; // danh sách bài viết của danh mục phụ
    public static final String KEY_NEWS_DETAILS = "link"; // key lúc gửi qua chi tiết bài viết
    public static long QUANTITY_NEWS_OF_CATE = 20; // số bài viết trong 1 danh mục
    public static String ROLE_ADMIN = "admin"; // quyền admin
    public static String ROLE_CUSTOMER = "user"; // quyền user
    public static String TABLE_USERS = "users"; // tên bảng users
    public static String TABLE_CATEGORIES = "categories"; // tên bảng danh mục

    // sửa số lượng bài viết trên trong danh mục
    public static void setQuantityNewsOfCate(int quantityNewsOfCate) {
        QUANTITY_NEWS_OF_CATE = quantityNewsOfCate;
    }
}
