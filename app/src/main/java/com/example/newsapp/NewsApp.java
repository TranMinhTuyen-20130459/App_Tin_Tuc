package com.example.newsapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

/**
 * Lớp này sẽ tự chạy khi ứng dụng bắt đầu.
 * Dùng để tạo NotificationChannel.
 */
public class NewsApp extends Application {
    public static final String CHANNEL_ID = "news_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        AlarmManager alarmManager = getSystemService(AlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(this, ViewedNewsReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pendingIntent);
    }

    /**
     * Tạo NotificationChannel.
     */
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
