package com.example.newsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.newsapp.AdminActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.UserListAdapter;
import com.example.newsapp.data.UsersDao;
import com.example.newsapp.models.Users;

import java.util.List;

public class EditUserDialogFragment extends androidx.fragment.app.DialogFragment {

    private EditText usernameEditText;
    private EditText nameEditText;

    private String currentUsername;
    private String currentName;

    public static EditUserDialogFragment newInstance(String username, String name) {
        EditUserDialogFragment fragment = new EditUserDialogFragment();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("name", name);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            currentUsername = args.getString("username");
            currentName = args.getString("name");
        }
        ManageUserFragment manageUserFragment = (ManageUserFragment) getTargetFragment();
//        if (manageUserFragment != null) {
//            // Cập nhật lại dữ liệu trên ManageUserFragment (nếu cần)
//            usersList = manageUserFragment.usersList;
//        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.fragment_edit_user_dialog, null);
        usernameEditText = dialogView.findViewById(R.id.editUsername);
        nameEditText = dialogView.findViewById(R.id.editName);

        if (!TextUtils.isEmpty(currentUsername)) {
            usernameEditText.setText(currentUsername);
        }

        if (!TextUtils.isEmpty(currentName)) {
            nameEditText.setText(currentName);
        }

        builder.setView(dialogView)
                .setTitle("Edit User")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Lưu thông tin người dùng sau khi nhấn nút "Save"
                        String username = usernameEditText.getText().toString();
                        String name = nameEditText.getText().toString();

                        // Gọi phương thức để xử lý dữ liệu đã nhập
                        saveUserInformation(currentUsername, username, name);
                        // Cập nhật lại AdminActivity.usersList
                        List<Users> list = AdminActivity.usersList;
                        for (Users user :
                                list) {
                            if (user.getUsername().equals(currentUsername)) {
                                user.setUsername(username);
                                user.setFullname(name);
                            }
                        }
                        // Tạo tham chiếu đến ManageUserFragment
                        ManageUserFragment manageUserFragment = (ManageUserFragment) getTargetFragment();
                        if (manageUserFragment != null) {
                            manageUserFragment.listView.invalidateViews();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditUserDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void saveUserInformation(String currentUsername, String username, String name) {
        // Xử lý dữ liệu đã nhập
        new UsersDao(getContext()).saveUser(currentUsername, username, name);
        // ...
    }
}