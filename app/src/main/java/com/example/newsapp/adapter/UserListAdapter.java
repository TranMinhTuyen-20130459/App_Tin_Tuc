package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.models.Users;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<Users> {
    private Context context;
    private List<Users> customerList;

    public UserListAdapter(Context context, List<Users> customerList) {
        super(context, android.R.layout.simple_list_item_1, customerList);
        this.context = context;
        this.customerList = customerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.customer_item, parent, false);

            holder = new ViewHolder();
            holder.txtUsername = convertView.findViewById(R.id.txt_username);
            holder.txtName = convertView.findViewById(R.id.txt_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Users customer = customerList.get(position);
        holder.txtUsername.setText(customer.getUsername());
        holder.txtName.setText(customer.getFullname());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtUsername;
        TextView txtName;
    }
}
