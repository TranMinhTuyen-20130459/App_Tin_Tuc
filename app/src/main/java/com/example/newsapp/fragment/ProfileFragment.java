package com.example.newsapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.LoginActivity;
import com.example.newsapp.R;
import com.example.newsapp.ViewedNewsActivity;
import com.example.newsapp.models.News;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    ArrayList<News> mDataList;
    ListView mListView;
    Button btn_login, btn_logout;
    TextView fullname;
    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Context context = getActivity();

        btn_login = view.findViewById(R.id.btn_login);
        btn_logout = view.findViewById(R.id.btn_logout);
        fullname = view.findViewById(R.id.fullname);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        auth = FirebaseAuth.getInstance();
        FirebaseUser userGoogle = auth.getCurrentUser();
        try {
            // Lấy User từ SharedPreferences
            String jvson = preferences.getString(Constants.ROLE_CUSTOMER, "");
            Users user = gson.fromJson(jvson, Users.class); // Chuyển đổi chuỗi JSON thành đối tượng User
            if(userGoogle != null){
                String name = userGoogle.getDisplayName();
                fullname.setText(name);
            }
            if (user == null && userGoogle == null && Constants.getLoginBy() == 100) {
                // Nếu người dùng chưa đăng nhập, ẩn nút đăng xuất
                btn_logout.setVisibility(View.GONE);
            } else {
                btn_logout.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                if(user != null){
                    // Nếu người dùng đã đăng nhập, hiển thị nút đăng xuất và đăng nhập
                    fullname.setText(user.getFullname());
                }else if(userGoogle != null){
                    String name = userGoogle.getDisplayName();
                    fullname.setText(name);
                }
            }
//            Toast.makeText(getActivity(), user.getFullname(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage());
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userGoogle != null){
                    auth.signOut();
                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                    btn_logout.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    fullname.setText("Trang cá nhân");
                    Constants.setLoginBy(100);
                }else{
                    SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove(Constants.ROLE_CUSTOMER);
                    editor.apply();
                    // Hiển thị thông báo cho người dùng
                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                    btn_logout.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    fullname.setText("Trang cá nhân");
                    Constants.setLoginBy(100);
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* Khi người dùng nhấn vào nút "Tin đã xem", mở ViewedNewsActivity để hiển thị các tin đã xem. */
        view.findViewById(R.id.btn_viewed).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewedNewsActivity.class)));
    }
}