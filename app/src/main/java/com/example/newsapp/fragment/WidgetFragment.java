package com.example.newsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.example.newsapp.utils.Constants;

public class WidgetFragment extends Fragment {

    Button btn_ok;
    TextView quantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment, container, false);

        btn_ok = view.findViewById(R.id.btn_ok);
        quantity = view.findViewById(R.id.quantity);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Hãy nhập số lượng bài viết cần hiển thị", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int quantityNewsOfCate = Integer.parseInt(quantity.getText().toString());

                        if (quantityNewsOfCate < 0) {
                            Toast.makeText(getContext(), "Số bài viết trong một danh mục không được âm", Toast.LENGTH_SHORT).show();
                        } else {
                            Constants.setQuantityNewsOfCate(quantityNewsOfCate);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
        return view;
    }
}