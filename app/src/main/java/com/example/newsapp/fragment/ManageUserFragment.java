package com.example.newsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.newsapp.AdminActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.UserListAdapter;
import com.example.newsapp.data.UsersDao;
import com.example.newsapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class ManageUserFragment extends Fragment {
    EditText editTextSearch;
    ListView listView;
    List<Users> usersList;
    public static List<Users> usersListFull;
    UserListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_user_fragment, container, false);
        listView = view.findViewById(R.id.list_view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            usersList = (List<Users>) bundle.getSerializable("dataList");
        }
        adapter = new UserListAdapter(getContext(), usersList);
        listView.setAdapter(adapter);
        adapter.setManageCustomerFragment(this);
        listView.setAdapter(adapter);

        editTextSearch = view.findViewById(R.id.search_user);
        if (usersListFull == null)
            usersListFull = (ArrayList) ((ArrayList) usersList).clone();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usersList.clear();
                String text = editTextSearch.getText().toString().toLowerCase();
                for (Users user :
                        usersListFull) {
                    if (user.getUsername().toLowerCase().contains(text)||user.getFullname().toLowerCase().contains(text)) {
                        usersList.add(user);
                    }
                }
                listView.invalidateViews();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void editUser(int position) {
        Users user = usersList.get(position);

        // Hiển thị dialog sửa thông tin người dùng
        EditUserDialogFragment dialogFragment = EditUserDialogFragment.newInstance(user.getUsername(), user.getFullname());
        dialogFragment.setTargetFragment(this, 0);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "edit_user_dialog");
    }

    public void deleteUser(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa người dùng");
        builder.setMessage("Bạn có chắc chắn muốn xóa người dùng này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng chọn xóa
                Users user = AdminActivity.usersList.get(position);
                String username = user.getUsername();
                // Gọi hàm xóa người dùng từ UsersDao
                new UsersDao(getContext()).deleteUser(username);
                listView.invalidateViews();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
