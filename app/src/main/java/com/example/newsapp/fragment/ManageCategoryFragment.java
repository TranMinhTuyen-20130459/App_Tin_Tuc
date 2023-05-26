package com.example.newsapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.RegisterActivity;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.data.CategoriesDao;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryFragment extends Fragment {
    ListView mListView;
    List<Categories> listCategory;
    CategoriesDao categoriesDao = new CategoriesDao();
    FirebaseDatabase db;
    DatabaseReference reference;
    public void init(View view) {
        listCategory = new ArrayList<>();
        categoriesDao.getAllCategoriesList(new CategoriesDao.CategoriesCallback<List<Categories>>(){
            @Override
            public void onSuccess(List<Categories> data){
                listCategory  =  data;
                // dùng adapter hiển thị lên listview category
                mListView = view.findViewById(R.id.list_view);
                CategoryAdapter adapter = new CategoryAdapter(getContext(), android.R.layout.simple_list_item_1, listCategory);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                System.out.println("Error: " + e.toString());
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_category_fragment, container, false);
        // đọc firebase hiển thị list categories
        init(view);

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
                showDialogForm(view);
            }
        });

        return view;

    }
    private void showDialogForm(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText etUrl = dialogView.findViewById(R.id.editTextUrl);
        EditText etTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText etId = dialogView.findViewById(R.id.editTextId);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = etUrl.getText().toString();
                String title = etTitle.getText().toString();
                String id = etId.getText().toString();

                // Kiểm tra tính hợp lệ của email
                if (url.isEmpty()) {
                    // Thực hiện các tác vụ khi người dùng nhấn nút "Thêm"
                    Toast.makeText(getContext(), "Vui lòng nhập url", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập id", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thêm categories vào Firebase
                db = FirebaseDatabase.getInstance();
                reference = db.getReference(Constants.TABLE_CATEGORIES);
                Categories categories = new Categories(id, url, title, "1");

                // Sử dụng title làm khóa
                reference.child(title).setValue(categories).addOnCompleteListener(
                   new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           init(view);
                           Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                       }
                   }
                );
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