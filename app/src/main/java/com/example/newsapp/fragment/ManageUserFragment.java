package com.example.newsapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.Serializable;
import java.util.List;

public class ManageUserFragment extends Fragment {
    ListView listView;
    List<Users> usersList;
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

//    public void reloadData() {
//        UsersDao usersDao = new UsersDao(getContext());
//        usersDao.getAllUserList(new UsersDao.UserCallback<List<Users>>() {
//            @Override
//            public void onSuccess(List<Users> data) {
//                // Dữ liệu người dùng đã được cập nhật thành công
//                // Cập nhật giao diện người dùng tại đây
//                usersList = data;
//                adapter.notifyDataSetChanged();
//
//                // Gửi dữ liệu vào fragment Customer
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("dataList", (Serializable) usersList);
////                EditUserDialogFragment editUserDialogFragment = new EditUserDialogFragment();
////                editUserDialogFragment.setArguments(bundle);
//                getParentFragmentManager().beginTransaction().commit();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                // Xử lý lỗi nếu có
//            }
//        });
//    }

}
