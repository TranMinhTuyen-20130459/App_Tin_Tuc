package com.example.newsapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 1;
    Button btn_login, btn_logout, btn_fllower;
    TextView fullname, if_email, if_name;
    FirebaseAuth auth;
    ImageView cameraIcon, coverImage, cameraIconAvata;
    LinearLayout ln_mail, ln_name;
    FrameLayout fr_cover_image;
    CircleImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Context context = getActivity();
        profileImage = view.findViewById(R.id.profile_image);
        btn_login = view.findViewById(R.id.btn_login);
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_fllower = view.findViewById(R.id.fllower);
        fullname = view.findViewById(R.id.fullname);
        if_email = view.findViewById(R.id.if_email);
        if_name = view.findViewById(R.id.if_name);

        fr_cover_image = view.findViewById(R.id.cover_image);
        ln_mail = view.findViewById(R.id.info_user);
        ln_name = view.findViewById(R.id.info_name);
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
        String jvson = preferences.getString(Constants.ROLE_CUSTOMER, "");
        Users user = gson.fromJson(jvson, Users.class); // Chuyển đổi chuỗi JSON thành đối tượng User
        try {
            // Lấy User từ SharedPreferences
            String name = "", username = "", email = "";
            if (user == null && userGoogle == null) {
                // Nếu người dùng chưa đăng nhập, ẩn nút đăng xuất
                btn_logout.setVisibility(View.GONE);
                ln_mail.setVisibility(View.GONE);
                ln_name.setVisibility(View.GONE);
                btn_fllower.setVisibility(View.GONE);
                fr_cover_image.setVisibility(View.GONE);
            } else {
                btn_logout.setVisibility(View.VISIBLE);
                ln_mail.setVisibility(View.VISIBLE);
                ln_name.setVisibility(View.VISIBLE);
                btn_fllower.setVisibility(View.VISIBLE);
                fr_cover_image.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                if (user != null) {
                    // Nếu người dùng đã đăng nhập, hiển thị nút đăng xuất và đăng nhập
                    name = user.getFullname();
                    email = user.getUsername();
                    profileImage.setImageResource(R.drawable.logo_nlu);

                } else if (userGoogle != null) {
                    name = userGoogle.getDisplayName();
                    email = userGoogle.getEmail();
                    profileImage.setImageResource(R.drawable.logo_nlu);
                }
                fullname.setText("Chào" + " " + getLastWord(name));
                if_email.setText(email);
                if_name.setText(name);
            }
//            Toast.makeText(getActivity(), user.getFullname(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage());
        }

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (userGoogle != null) {
                                    auth.signOut();
                                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                                    btn_logout.setVisibility(View.GONE);
                                    ln_mail.setVisibility(View.GONE);
                                    ln_name.setVisibility(View.GONE);
                                    btn_fllower.setVisibility(View.GONE);
                                    fr_cover_image.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    fullname.setText("");
                                } else {
                                    SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove(Constants.ROLE_CUSTOMER);
                                    editor.apply();
                                    // Hiển thị thông báo cho người dùng
                                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_LONG).show();
                                    btn_logout.setVisibility(View.GONE);
                                    ln_mail.setVisibility(View.GONE);
                                    ln_name.setVisibility(View.GONE);
                                    btn_fllower.setVisibility(View.GONE);
                                    fr_cover_image.setVisibility(View.GONE);
                                    btn_login.setVisibility(View.VISIBLE);
                                    fullname.setText("");
                                }
                                profileImage.setImageResource(R.drawable.btn_user);

                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }

        });
        btn_fllower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chức năng này đang được phát triển!", Toast.LENGTH_SHORT).show();
            }
        });
        cameraIcon = view.findViewById(R.id.camera_icon);
        coverImage = view.findViewById(R.id.cover_image_item);

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        cameraIconAvata = view.findViewById(R.id.camera_icon_avata);
        cameraIconAvata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        if (user != null || userGoogle != null) {
            // Kiểm tra xem có đường dẫn hình ảnh đã lưu trong SharedPreferences không
            SharedPreferences preferences1 = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
            String imagePath = preferences1.getString("imagePath", null);
            if (imagePath != null) {
                // Đã lưu đường dẫn hình ảnh trong SharedPreferences
                Uri imageUri = Uri.parse(imagePath);
                coverImage.setImageURI(imageUri);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* Khi người dùng nhấn vào nút "Tin đã xem", mở ViewedNewsActivity để hiển thị các tin đã xem. */
        view.findViewById(R.id.btn_viewed).setOnClickListener(v -> startActivity(new Intent(requireContext(), ViewedNewsActivity.class)));
    }


    private String getLastWord(String name) {
        String[] textArray = name.trim().split("\\s+");
        if (textArray.length > 0) {
            return textArray[textArray.length - 1];
        }
        return name;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            if (data != null) {
                Uri imageUri = data.getData();
                // Thay đổi hình ảnh của cover_image thành hình ảnh đã chọn
                coverImage.setImageURI(imageUri);
                // Lưu hình ảnh vào bộ nhớ ứng dụng
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {

        deletePreviousImage();
        // Tạo tên tệp tin duy nhất
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";

        // Tạo đường dẫn cho tệp tin hình ảnh trong bộ nhớ ứng dụng
        File directory = getActivity().getFilesDir();
        File imageFile = new File(directory, imageFileName);

        try {
            // Lưu hình ảnh vào tệp tin
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("imagePath", imageFile.getAbsolutePath());
            editor.apply();

            // Hiển thị thông báo cho người dùng
            Toast.makeText(getActivity(), "Hình ảnh đã được lưu", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePreviousImage() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String previousImagePath = preferences.getString("imagePath", null);
        if (previousImagePath != null) {
            // Xóa hình ảnh trước đó
            File previousImageFile = new File(previousImagePath);
            previousImageFile.delete();

            // Xóa đường dẫn hình ảnh trong SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("imagePath");
            editor.apply();
        }
    }

}