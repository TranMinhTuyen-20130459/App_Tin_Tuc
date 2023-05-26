package com.example.newsapp.data;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.newsapp.AdminActivity;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersDao {
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private Context context;

    public UsersDao(Context context) {
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(Constants.TABLE_USERS);
        this.context = context;
    }

    public void getAllUserList(final UserCallback<List<Users>> callback) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Users> usersList = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String role = childSnapshot.child("role").getValue(String.class);
                    if (role.equals(Constants.ROLE_CUSTOMER)) {
                        String username = childSnapshot.child("username").getValue(String.class);
                        String password = childSnapshot.child("password").getValue(String.class);
                        String fullname = childSnapshot.child("fullname").getValue(String.class);
                        usersList.add(new Users(role, username, password, fullname));
                    }
                }
                callback.onSuccess(usersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.toException());
            }
        });
    }

    public void saveUser(String currentUsername,final String username, final String name) {

        reference.orderByChild("username")
                .equalTo(currentUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            // Cập nhật thông tin người dùng
                            userSnapshot.getRef().child("username").setValue(username);
                            userSnapshot.getRef().child("fullname").setValue(name);

                            // Hiển thị thông báo thành công
                            Toast.makeText(context, "Lưu người dùng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi trong quá trình truy vấn
                        Toast.makeText(context, "Lỗi truy vấn cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteUser(final String username) {
        reference.orderByChild("username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            userSnapshot.getRef().removeValue();
                            for (Users user :
                                    AdminActivity.usersList) {
                                if (user.getUsername().equals(username)) {
                                    AdminActivity.usersList.remove(user);
                                    break;
                                }
                            }
                            // Hiển thị thông báo xóa thành công
                            Toast.makeText(context, "Xóa người dùng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi trong quá trình truy vấn
                        Toast.makeText(context, "Lỗi truy vấn cơ sở dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public interface UserCallback<T> {
        void onSuccess(T data);

        void onError(Exception e);
    }
}
