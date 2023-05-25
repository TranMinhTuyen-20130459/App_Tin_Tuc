package com.example.newsapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.newsapp.NewsDetailActivity;
import com.example.newsapp.R;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.adapter.DataListAdapter;
import com.example.newsapp.models.Categories;
import com.example.newsapp.utils.Constants;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageCategoryFragment extends Fragment {
    ListView mListView;
    ArrayList<Categories> listCategory;
    public void init() {
        listCategory = new ArrayList<>();
        // Tạo danh sách đối tượng Person
        listCategory.add(new Categories("1", "https://vnexpress.net/rss/tin-moi-nhat.rss", "tin mới", "1"));
        listCategory.add(new Categories("2", "https://vnexpress.net/rss/tin-moi-nhat.rss", "thể thao", "1"));
        listCategory.add(new Categories("3", "https://vnexpress.net/rss/tin-moi-nhat.rss", "thời sự", "1"));
        listCategory.add(new Categories("4", "https://vnexpress.net/rss/tin-moi-nhat.rss", "giáo dục", "1"));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_category_fragment, container, false);
        init();

        // dùng adapter hiển thị lên listview category
        mListView = view.findViewById(R.id.list_view);
        CategoryAdapter adapter = new CategoryAdapter(getContext(), android.R.layout.simple_list_item_1, listCategory);
        mListView.setAdapter(adapter);

        // button search, tìm kiếm
        Button btnSearch = view.findViewById(R.id.btnSearchCate);
        EditText etSearch = view.findViewById(R.id.search_name);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // xử lí tìm kiếm
                String name = etSearch.getText().toString().trim();
                ArrayList<Categories> listCategorySearch = new ArrayList<>();

                for(Categories c : listCategory) {
                    if(c.getTitle().contains(name)) listCategorySearch.add(c);
                }

                // dùng adapter hiển thị lên listview category
                mListView = view.findViewById(R.id.list_view);
                CategoryAdapter adapter = new CategoryAdapter(getContext(), android.R.layout.simple_list_item_1, listCategorySearch);
                mListView.setAdapter(adapter);
            }
        });


        // form add category
        Button btnAddCate = view.findViewById(R.id.btnAddCate);
        btnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị hộp thoại form Dialog
                showDialogForm();
            }
        });

        return view;

    }
    private void showDialogForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText etUrl = dialogView.findViewById(R.id.editTextUrl);
        EditText etTitle = dialogView.findViewById(R.id.editTextTitle);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = etUrl.getText().toString();
                String title = etTitle.getText().toString();

                // Thực hiện các tác vụ khi người dùng nhấn nút "Thêm"
                Toast.makeText(getContext(), "Thêm danh mục: " + title, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}