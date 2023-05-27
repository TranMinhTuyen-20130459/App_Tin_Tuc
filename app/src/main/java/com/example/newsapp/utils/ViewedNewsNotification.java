package com.example.newsapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.newsapp.ViewedNewsReceiver;

public class ViewedNewsNotification {
    private final Context context;

    public ViewedNewsNotification(Context context) {
        this.context = context;
    }

    public void scheduleNext() {
        AlarmManager alarmManager = context.getSystemService(AlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(context, ViewedNewsReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pendingIntent);
    }
}
