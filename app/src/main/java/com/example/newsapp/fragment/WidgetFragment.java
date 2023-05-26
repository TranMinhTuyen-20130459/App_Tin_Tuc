package com.example.newsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.utils.Constants;

public class WidgetFragment extends Fragment {

    Button  btn_ok;
    TextView  quantity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment, container, false);

        btn_ok = view.findViewById(R.id.btn_ok);
        quantity = view.findViewById(R.id.quantity);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.setQuantityNewsOfCate(Integer.parseInt(quantity.getText().toString()));
            }
        });
        return view;
    }
}