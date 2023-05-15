package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapp.R;
import com.example.newsapp.SearchActivity;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.SearchNews;

import java.util.ArrayList;
import java.util.List;

public class HistorySearchAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private List<String> list_data;

    public HistorySearchAdapter(@NonNull Context context, int resource, @NonNull List<String> listHistorySearch) {
        super(context, resource, listHistorySearch);
        this.ctx = context;
        this.list_data = listHistorySearch;
    }

    @Override
    public int getCount() {
        return list_data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(R.layout.item_history_search, parent, false);
        }

        // thực hiện đổ dữ liệu vào itemView
        TextView item_text_view_history_search = itemView.findViewById(R.id.text_view_history_search);
        item_text_view_history_search.setText(list_data.get(position));
        item_text_view_history_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = item_text_view_history_search.getText().toString();
                ArrayList<News> list_result_search = (ArrayList<News>) SearchNews.searchByKeyWord(SearchActivity.list_all_news, keyword);

                Toast.makeText(ctx, list_result_search.size() + "", Toast.LENGTH_SHORT).show();
                // Toast.makeText(getContext(), keyword, Toast.LENGTH_SHORT).show();
            }
        });
        return itemView;

    }
}
