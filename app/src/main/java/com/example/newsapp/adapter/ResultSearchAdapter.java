package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapp.R;
import com.example.newsapp.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultSearchAdapter extends ArrayAdapter<News> {

    private Context ctx;
    private List<News> list_data;

    public ResultSearchAdapter(@NonNull Context context, int resource, @NonNull List<News> list_data_news) {
        super(context, resource, list_data_news);
        this.ctx = context;
        this.list_data = list_data_news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;

        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.news_item, parent, false);
        }

        TextView tv_title = itemView.findViewById(R.id.title);
        TextView tv_time = itemView.findViewById(R.id.time);
        ImageView image = itemView.findViewById(R.id.img);

        // thực hiện đổ dữ liệu vào itemView
        tv_title.setText(list_data.get(position).getTitle());
        tv_time.setText(list_data.get(position).getDate());
        Picasso.get().load(list_data.get(position).getLinkImage()).into(image);

        return itemView;
    }
}
