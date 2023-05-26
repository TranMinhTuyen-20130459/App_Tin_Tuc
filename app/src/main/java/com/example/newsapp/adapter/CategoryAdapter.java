package com.example.newsapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newsapp.R;
import com.example.newsapp.models.Categories;
import com.example.newsapp.models.News;
import com.example.newsapp.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Categories> {
    Context myContext;
    int mResource;
    List<Categories> myListCategory;

    FirebaseDatabase db;
    DatabaseReference reference;

    public CategoryAdapter(Context context, int resource, List<Categories> listCategory) {
        super(context, resource, listCategory);
        this.myContext = context;
        this.mResource = resource;
        this.myListCategory = listCategory;
    }

    @Override
    public int getCount() {
        return myListCategory.size();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final String title = myListCategory.get(position).getTitle();
        final String id = myListCategory.get(position).getPosition();
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.row_category, null);
        }

        //Ánh xạ gán giá trị
        TextView txtSTT = (TextView) view.findViewById(R.id.id_category_stt);
        txtSTT.setText(myListCategory.get(position).getPosition());

        TextView txtTitle = (TextView) view.findViewById(R.id.id_category_title);
        txtTitle.setText(myListCategory.get(position).getTitle());

        TextView txtActive = (TextView) view.findViewById(R.id.id_category_active);
        txtActive.setText(myListCategory.get(position).getActive());

        ImageView delete = view.findViewById(R.id.delete_cate);
        ImageView edit = view.findViewById(R.id.edit_cate);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các tác vụ khi nút "Delete" được click trong item của ListView
                // Hiển thị hộp thoại xác nhận xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có chắc muốn xóa '" + title + "' không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();

                        // Thêm categories vào Firebase
                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference(Constants.TABLE_CATEGORIES);
                        Categories categories = myListCategory.get(position);
                        //cập nhập giá trị hiện tại
                        myListCategory.get(position).setActive("0");
                        categories.setActive("0");
                        // Sử dụng title làm khóa
                        reference.child(id).setValue(categories).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Category");

                // Inflate layout cho hộp thoại
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView = inflater.inflate(R.layout.dialog_edit_category, null);
                builder.setView(dialogView);

                // Ánh xạ các thành phần trong hộp thoại
                EditText etTitle = dialogView.findViewById(R.id.etTitle);
                EditText etUrl = dialogView.findViewById(R.id.etUrl);
                Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);
                TextView etId = dialogView.findViewById(R.id.etId);

                // Lấy dữ liệu từ danh mục hiện tại
                String currentTitle = myListCategory.get(position).getTitle();
                String currentUrl = myListCategory.get(position).getUrl();
                String currentStatus = myListCategory.get(position).getActive();
                String currentId = myListCategory.get(position).getPosition();

                // Đặt giá trị ban đầu cho các thành phần trong hộp thoại
                etId.setText(currentId);
                etTitle.setText(currentTitle);
                etUrl.setText(currentUrl);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"0", "1"});
                spinnerStatus.setAdapter(adapter);
                spinnerStatus.setAdapter(adapter);
//                spinnerStatus.setSelection(Integer.parseInt(currentStatus));
                spinnerStatus.setSelection(currentStatus.equals("1") ? 1 : 0);
                // Xử lý logic khi nhấn nút "Save" trong hộp thoại
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy giá trị từ các thành phần trong hộp thoại sau khi người dùng thay đổi
                        String editedTitle = etTitle.getText().toString().trim();
                        String editedUrl = etUrl.getText().toString().trim();
                        String editedStatus = String.valueOf(spinnerStatus.getSelectedItemPosition());
                        String editedId = etId.getText().toString().trim();
                        // Cập nhật giá trị của danh mục trong danh sách và cập nhật giao diện
                        myListCategory.get(position).setTitle(editedTitle);
                        myListCategory.get(position).setUrl(editedUrl);
                        myListCategory.get(position).setActive(editedStatus);
                        myListCategory.get(position).setPosition(editedId);
                        notifyDataSetChanged();

                        // cập nhập xuống firebase

                        // Thêm categories vào Firebase
                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference(Constants.TABLE_CATEGORIES);
                        Categories categories = new Categories(editedId, editedUrl, editedTitle, editedStatus);

                        // Sử dụng title làm khóa
                        reference.child(id).setValue(categories).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                    }
                                }
                        );
                    }
                });

                // Xử lý logic khi nhấn nút "Cancel" trong hộp thoại
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không thực hiện thay đổi khi nhấn nút "Cancel"
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }

    private static class ViewHolder {
        TextView txtSTT;
        TextView txtTitle;
        TextView txtActive;
        ImageView deleteButton;
        ImageView editButton;
    }
}