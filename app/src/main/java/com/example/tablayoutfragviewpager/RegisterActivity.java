package com.example.tablayoutfragviewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tablayoutfragviewpager.databinding.ActivityRegisterBinding;
import com.example.tablayoutfragviewpager.models.Users;
import com.example.tablayoutfragviewpager.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    String username, password, fullname;
    int role;
    Users user;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sử dụng binding để lấy views
        EditText usernameEditText = binding.username;
        EditText passwordEditText = binding.password;
        EditText repasswordEditText = binding.repassword;
        EditText fullnameEditText = binding.fullname;
        Button registerButton = binding.btnRegister;
        TextView haveAnAccountTextView = binding.btnHaveAnAccount;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ các EditText
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                fullname = fullnameEditText.getText().toString();
                int role = Constants.ROLE_CUSTOMER;
                String repassword = repasswordEditText.getText().toString();

                // Kiểm tra tính hợp lệ của email
                if (!validate(username)) {
                    usernameEditText.setError("Vui lòng nhập địa chỉ email hợp lệ!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("Vui lòng nhập mật khẩu!");
                    return;
                }
                if (!password.equals(repassword)) {
                    repasswordEditText.setError("Mật khẩu không khớp!");
                    return;
                }

                // Thêm user vào Firebase
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("users");
                // Kiểm tra xem email đã tồn tại trước đó trong Firebase chưa
                reference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Nếu email đã được sử dụng trước đó, hiển thị lỗi và không thực hiện đăng ký
                                if (dataSnapshot.exists()) {
                                    usernameEditText.setError("Email này đã được đăng ký trước đó!");
                                    return;
                                } else {
                                    // Nếu email chưa được sử dụng, thêm user vào Firebase
                                    user = new Users(role,username, password, fullname);
                                    reference.child(username.replace(".", ",")).setValue(user).
                                            addOnCompleteListener(
                                                    new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            usernameEditText.setText("");
                                                            passwordEditText.setText("");
                                                            repasswordEditText.setText("");
                                                            fullnameEditText.setText("");
                                                            Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                            );
                                    // Chuyển đến màn hình LoginActivity
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Xử lý lỗi khi thao tác với Firebase
                                Toast.makeText(RegisterActivity.this, "Đã xảy ra lỗi khi đăng ký tài khoản", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    public boolean validate(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}