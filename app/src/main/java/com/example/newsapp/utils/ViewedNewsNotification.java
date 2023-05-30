package com.example.newsapp.utils;

import static com.example.newsapp.NewsApp.CHANNEL_ID;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.service.notification.StatusBarNotification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.ViewedNewsReceiver;
import com.example.newsapp.models.News;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

public class ViewedNewsNotification {
    private static final String NEWS_TAG = "news_tag";
    private static final int NOTIFICATIONS_ALLOWED = 3;

    private final Context context;
    private final AlarmManager alarmManager;
    private final NotificationManager notificationManager;

    public ViewedNewsNotification(@NonNull Context context) {
        this.context = context.getApplicationContext();
        alarmManager = context.getSystemService(AlarmManager.class);
        notificationManager = context.getSystemService(NotificationManager.class);
    }

    public void scheduleNext() {
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                new Intent(context, ViewedNewsReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pi);
    }

    public boolean isFull() {
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        return (int) Arrays.stream(activeNotifications).filter(n -> n.getTag().equals(NEWS_TAG)).count() >= NOTIFICATIONS_ALLOWED;
    }

    public boolean isActive(int id) {
        StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
        return activeNotifications.length != 0 && Arrays.stream(activeNotifications).anyMatch(n -> n.getTag().equals(NEWS_TAG) && n.getId() == id);
    }

    public void pushNotification(@NonNull News news) {
        Picasso.get().load(news.getLinkImage()).into(new ImageHelper.TargetAdapter() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent intent = new Intent(context, NewsDetailActivity.class)
                        .putExtra(Constants.KEY_NEWS_DETAILS, news.getLink())
                        .putExtra(Constants.KEY_VIEWED_NEWS, news);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context)
                        .addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(news.getTitle()))
                        .setContentIntent(pendingIntent)
                        .setShowWhen(true)
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                notificationManager.notify(NEWS_TAG, news.getLink().hashCode(), builder.build());
            }
        });
    }
}