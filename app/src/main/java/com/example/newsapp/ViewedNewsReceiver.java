package com.example.newsapp;

import static com.example.newsapp.NewsApp.CHANNEL_ID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.newsapp.data.remote.RssFetch;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.example.newsapp.utils.XMLDOMParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewedNewsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new RssFetch().fetchRss(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    XMLDOMParser xmldomParser = new XMLDOMParser();
                    News news = xmldomParser.parse(response.body());
                    if (news != null) {
                        pushNotification(context, news);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void pushNotification(Context context, @NonNull News news) {
        Picasso.get().load(news.getLinkImage()).into(new Target() {
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
                        .setContentTitle(news.getTitle())
                        .setContentText(news.getDescription())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(news.getDescription()))
                        .setContentIntent(pendingIntent)
                        .setShowWhen(true)
                        .setAutoCancel(true)
                        .setLargeIcon(bitmap)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.notify(78, builder.build());
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Do nothing
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Do nothing
            }
        });
    }
}