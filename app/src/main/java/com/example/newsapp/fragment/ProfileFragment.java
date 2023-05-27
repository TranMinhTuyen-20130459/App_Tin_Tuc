package com.example.newsapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.newsapp.utils.Constants.MY_PREFERENCES;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.LoginActivity;
import com.example.newsapp.R;
import com.example.newsapp.RegisterActivity;
import com.example.newsapp.ViewedNewsActivity;
import com.example.newsapp.databinding.ProfileFragmentBinding;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class ProfileFragment extends Fragment {
    private ProfileFragmentBinding binding;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);

        binding.btnLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        binding.btnSignUp.setOnClickListener(v -> startActivity(new Intent(getActivity(), RegisterActivity.class)));

        SharedPreferences preferences = requireContext().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        auth = FirebaseAuth.getInstance();
        FirebaseUser userGoogle = auth.getCurrentUser();
        try {
            // Lấy User từ SharedPreferences
            String jvson = preferences.getString(Constants.ROLE_CUSTOMER, "");
            Users user = gson.fromJson(jvson, Users.class); // Chuyển đổi chuỗi JSON thành đối tượng User
            if (user != null) {
                // Nếu người dùng đã đăng nhập, hiển thị thông tin người dùng
                binding.avatarFullName.setText(user.getFullname());
                binding.avatarRole.setText(user.getRole());
                binding.name.setText(user.getFullname());
                binding.email.setText(user.getUsername());
                binding.role.setText(user.getRole());
                binding.noLogin.setVisibility(View.GONE);
                binding.profile.setVisibility(View.VISIBLE);
            }
            if(userGoogle != null){
                binding.avatarFullName.setText(userGoogle.getDisplayName());
                binding.avatarRole.setText("user");
                binding.name.setText(userGoogle.getDisplayName());
                binding.email.setText(userGoogle.getEmail());
                binding.role.setText("user");
                binding.noLogin.setVisibility(View.GONE);
                binding.profile.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        binding.logout.setOnClickListener(v -> {
            if(userGoogle != null){
                auth.signOut();
                Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                binding.profile.setVisibility(View.GONE);
                binding.noLogin.setVisibility(View.VISIBLE);
            }else{
                SharedPreferences preferences1 = requireContext().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.remove(Constants.ROLE_CUSTOMER);
                editor.apply();
                // Hiển thị thông báo cho người dùng
                Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                binding.profile.setVisibility(View.GONE);
                binding.noLogin.setVisibility(View.VISIBLE);
            }


            SharedPreferences preferences1 = requireContext().getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences1.edit();
            editor.remove(Constants.ROLE_CUSTOMER);
            editor.apply();
            // Hiển thị thông báo cho người dùng
            Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
            binding.profile.setVisibility(View.GONE);
            binding.noLogin.setVisibility(View.VISIBLE);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* Khi người dùng nhấn vào nút "Tin đã xem", mở ViewedNewsActivity để hiển thị các tin đã xem. */
        view.findViewById(R.id.viewed).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewedNewsActivity.class)));
    }
}