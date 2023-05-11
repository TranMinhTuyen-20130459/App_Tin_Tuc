package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.databinding.ActivityLoginBinding;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    TextView txt_register;
    String email, password, fullname, role;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        txt_register = binding.tvRegister;
        etLoginEmail = binding.etLoginEmail;
        etLoginPassword = binding.etLoginPassword;
        btnLogin = binding.btnLogin;

        // tham chiếu đến bảng users
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(Constants.TABLE_USERS);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Kiểm tra tính hợp lệ của email
                if (!validate(etLoginEmail.getText().toString())) {
                    etLoginEmail.setError("Vui lòng nhập địa chỉ email hợp lệ!");
                    return;
                }
                if (TextUtils.isEmpty(etLoginPassword.getText().toString())) {
                    etLoginPassword.setError("Vui lòng nhập mật khẩu!");
                    return;
                }

                // lấy dữ liệu lên
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // lấy thẻ trên database
                        email = snapshot.child(etLoginEmail.getText().toString().replace(".", ",")).child("username").getValue(String.class);
                        password = snapshot.child(etLoginEmail.getText().toString().replace(".", ",")).child("password").getValue(String.class);
                        fullname = snapshot.child(etLoginEmail.getText().toString().replace(".", ",")).child("fullname").getValue(String.class);
                        role = snapshot.child(etLoginEmail.getText().toString().replace(".", ",")).child("role").getValue(String.class);
                        if (email != null && etLoginPassword.getText().toString().equals(password)) {
                            user = new Users(role, email, password, fullname);
                            SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(user); // Chuyển đổi User thành chuỗi JSON
                            // chuyển đến MainActivity
                            Intent intent = null;
                            if (role.equals(Constants.ROLE_CUSTOMER)) {
                                editor.putString(Constants.ROLE_CUSTOMER, json);
                                editor.apply();
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tên " + user.getFullname(), Toast.LENGTH_LONG).show();
                            }
                            if (role.equals(Constants.ROLE_ADMIN)) {
                                editor.putString(Constants.ROLE_ADMIN, json);
                                editor.apply();
                                intent = new Intent(LoginActivity.this, AdminActivity.class);
                                Toast.makeText(LoginActivity.this, "Đăng nhập ADMIN", Toast.LENGTH_LONG).show();
                            }
                            // xóa toàn bộ stack trước đó
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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