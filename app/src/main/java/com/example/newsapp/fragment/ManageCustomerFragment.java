package com.example.newsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.AdminActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.UserListAdapter;
import com.example.newsapp.models.Users;

import java.util.List;

public class ManageCustomerFragment extends Fragment {
    ListView listView;
    List<Users> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_customer_fragment, container, false);
        listView = view.findViewById(R.id.list_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            usersList = (List<Users>) bundle.getSerializable("dataList");
        }
        UserListAdapter adapter = new UserListAdapter(getContext(), usersList);
        listView.setAdapter(adapter);

        return view;
    }
}