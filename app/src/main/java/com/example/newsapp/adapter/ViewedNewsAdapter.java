package com.example.newsapp.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.ImageHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewedNewsAdapter extends RecyclerView.Adapter<ViewedNewsAdapter.ViewedNewsHolder> {
    private final List<News> news;
    private final OnNewsClickListener listener;

    public interface OnNewsClickListener {
        void onNewsClicked(int position);

        boolean onNewsLongClicked(int position);
    }

    public ViewedNewsAdapter(@NonNull List<News> news, OnNewsClickListener listener) {
        this.news = news;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewedNewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewedNewsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewedNewsHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewedNewsHolder extends RecyclerView.ViewHolder {
        private final View root;
        private final TextView textTitle, textTime;
        private final ImageView imageNews;

        public ViewedNewsHolder(@NonNull View itemView) {
            super(itemView);
            this.root = itemView;
            textTitle = itemView.findViewById(R.id.title);
            textTime = itemView.findViewById(R.id.time);
            imageNews = itemView.findViewById(R.id.img);
        }

        public void bind(News news) {
            Picasso.get().load(news.getLinkImage()).into(new ImageHelper.TargetAdapter() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Bitmap rounded = ImageHelper.getRoundedCornerBitmap(bitmap, 5);
                    imageNews.setImageBitmap(rounded);
                }
            });
            textTitle.setText(news.getTitle());
            textTime.setText(news.getDate());
            root.setSelected(news.isSelected());
            root.setOnClickListener(v -> listener.onNewsClicked(getBindingAdapterPosition()));
            root.setOnLongClickListener(v -> listener.onNewsLongClicked(getBindingAdapterPosition()));
        }
    }
}