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

import com.example.newsapp.models.News;
import com.example.newsapp.R;
import com.example.newsapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataListAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;
    private ArrayList<News> mDataList;

    public DataListAdapter(Context context, int resource, ArrayList<News> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.mResource = resource;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.news_item, null);
        }

        TextView title = view.findViewById(R.id.title);
        TextView timer = view.findViewById(R.id.time);
        ImageView image = view.findViewById(R.id.img);
        title.setText(mDataList.get(position).getTitle());
        title.setTextSize(Constants.FONT_SIZE);
        timer.setText(mDataList.get(position).getDate());
        try {
        Picasso.get().load(mDataList.get(position).getLinkImage()).into(image);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return view;
    }
}

