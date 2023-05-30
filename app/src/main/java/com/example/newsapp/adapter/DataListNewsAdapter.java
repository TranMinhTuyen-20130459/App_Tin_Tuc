package com.example.newsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.newsapp.utils.ImageHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataListNewsAdapter extends ArrayAdapter<News> {
    private Context mContext;
    private int mResource;
    private ArrayList<News> mDataList;

    public DataListNewsAdapter(Context context, int resource, ArrayList<News> dataList) {
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
        try {
            TextView title = view.findViewById(R.id.title);
            TextView timer = view.findViewById(R.id.time);
            ImageView image = view.findViewById(R.id.img);

            Picasso.get().load(mDataList.get(position).getLinkImage()).into(new ImageHelper.TargetAdapter() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Bitmap rounded = ImageHelper.getRoundedCornerBitmap(bitmap, 5);
                    image.setImageBitmap(rounded);
                }
            });
            title.setText(mDataList.get(position).getTitle());
            timer.setText(mDataList.get(position).getDate());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return view;
    }
}

