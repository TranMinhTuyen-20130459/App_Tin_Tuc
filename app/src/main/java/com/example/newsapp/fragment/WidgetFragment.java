package com.example.newsapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.utils.Constants;

import androidx.core.content.ContextCompat;
public class WidgetFragment extends Fragment {
    private TextView textView;
    TextView font_size, quantity, test;

    TextView title;
    int fontsize = 0;
    static int checkedItem = 1;

    int font;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment, container, false);
        font_size = view.findViewById(R.id.font_size);
        quantity = view.findViewById(R.id.quantity);
        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chỉnh số bài viết");

                // Tạo một EditText để nhập số
                final EditText inputEditText = new EditText(getContext());
                inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER); // Chỉ cho phép nhập số
                inputEditText.setHint("Số bài viết"); // Gợi ý trong ô nhập

                // Tạo một icon số bài viết
                builder.setView(inputEditText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = inputEditText.getText().toString();
                        if (!TextUtils.isEmpty(inputText)) {
                            int articleCount = Integer.parseInt(inputText);
                            if (articleCount > 0 && articleCount < 60) {
                                // Sử dụng giá trị articleCount tùy ý ở đây
                                Constants.setQuantityNewsOfCate(articleCount);
                            } else {
                                // Hiển thị thông báo lỗi nếu số không thỏa mãn điều kiện
                                Toast.makeText(getContext(), "Số bài viết không hợp lệ!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                builder.setNegativeButton("Hủy", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        font_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Chọn cỡ chữ");

                final String[] fontSizeOptions = {"Nhỏ", "Bình thường", "Lớn"};
                // Vị trí của "Bình thường" trong mảng fontSizeOptions

                builder.setSingleChoiceItems(fontSizeOptions, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Nhỏ
                                font = 14;
                                checkedItem = 0;
                                break;
                            case 1: // Bình thường
                                font = 19;
                                checkedItem = 1;
                                break;
                            case 2: // Lớn
                                font = 23;
                                checkedItem = 2;
                                break;
                            default:
                                break;
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.setFontSize(font);
                    }
                });

                builder.setNegativeButton("Hủy", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
        return view;

    }
}