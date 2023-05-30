package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.databinding.ActivityLoginBinding;
import com.example.newsapp.models.GoogleUser;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    DatabaseReference reference;
    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    TextView txt_register;
    String email, password, fullname, role;
    Users user;

    ImageView google;
    FirebaseAuth auth;
    FirebaseDatabase db;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pre = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        boolean isDark = pre.getBoolean("is_dark", false);
        setTheme(isDark ? R.style.AppThemeDark : R.style.AppTheme);
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
                        DataSnapshot userData = snapshot.child(etLoginEmail.getText().toString().replace(".", ","));
                        email = userData.child("username").getValue(String.class);
                        password = userData.child("password").getValue(String.class);
                        fullname = userData.child("fullname").getValue(String.class);
                        role = userData.child("role").getValue(String.class);

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

        google = binding.btnGoogleLoginPage;
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("we are creating your account");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
//        if(auth.getCurrentUser() != null){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
    int RC_SIGN_IN = 40;
    public void googleSignIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }catch (Exception e){
                Toast.makeText(this, "lỗi ở đây nè", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            GoogleUser googleUser = new GoogleUser();
                            googleUser.setUserId(user.getUid());
                            googleUser.setName(user.getDisplayName());
                            googleUser.setProfile(user.getPhotoUrl().toString());
                            db.getReference().child("google_users").child(user.getUid()).setValue(googleUser);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tên " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
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