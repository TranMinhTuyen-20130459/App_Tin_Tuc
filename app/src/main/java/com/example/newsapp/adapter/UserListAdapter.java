package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.fragment.ManageUserFragment;
import com.example.newsapp.models.Users;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<Users> {
    private Context context;
    private List<Users> usersList;
    private ManageUserFragment fragment;

    public UserListAdapter(Context context, List<Users> usersList) {
        super(context, android.R.layout.simple_list_item_1, usersList);
        this.context = context;
        this.usersList = usersList;
    }

    public void setManageCustomerFragment(ManageUserFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.user_item, parent, false);

            holder = new ViewHolder();
            holder.txtUsername = convertView.findViewById(R.id.txt_username);
            holder.txtName = convertView.findViewById(R.id.txt_name);
            holder.imageButtonEdit = convertView.findViewById(R.id.btn_edit);
            holder.imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment != null) {
                        fragment.editUser(position);
                    }
                }
            });
            holder.imageButtonDelete = convertView.findViewById(R.id.btn_delete);
            holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment != null) {
                        fragment.deleteUser(position);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Users customer = usersList.get(position);
        holder.txtUsername.setText(customer.getUsername());
        holder.txtName.setText(customer.getFullname());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtUsername;
        TextView txtName;
        ImageButton imageButtonEdit;
        ImageButton imageButtonDelete;
    }
}
