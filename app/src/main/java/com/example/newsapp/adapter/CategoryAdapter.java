package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapp.R;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.News;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Categories> {
    Context myContext;
    int mResource;
    List<Categories> myListCategory;

    public CategoryAdapter(Context context, int resource, List<Categories> listCategory) {
        super(context, resource, listCategory);
        this.myContext = context;
        this.mResource = resource;
        this.myListCategory = listCategory;
    }

    @Override
    public int getCount() {
        return myListCategory.size();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.row_category, null);
        }

        //Ánh xạ gán giá trị
        TextView txtSTT = (TextView) view.findViewById(R.id.id_category_stt);
        txtSTT.setText(myListCategory.get(position).getPosition());

        TextView txtTitle = (TextView) view.findViewById(R.id.id_category_title);
        txtTitle.setText(myListCategory.get(position).getTitle());

        TextView txtActive = (TextView) view.findViewById(R.id.id_category_active);
        txtActive.setText(myListCategory.get(position).getActive());

        Categories category = myListCategory.get(position);
        ImageView delete = view.findViewById(R.id.delete_cate);
        ImageView edit = view.findViewById(R.id.edit_cate);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các tác vụ khi nút "Delete" được click trong item của ListView
                System.out.println("Hello");
                System.out.println(txtTitle);
                System.out.println(txtSTT);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các tác vụ khi nút "Edit" được click trong item của ListView
                System.out.println("Hellllllo");
            }
        });

        return view;
    }

    private static class ViewHolder {
        ImageView editButton;
        // Các view khác trong item
        // ...
    }
}
