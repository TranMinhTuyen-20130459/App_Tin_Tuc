package com.example.newsapp.data;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.example.newsapp.models.Categories;
import com.example.newsapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDao {
    private FirebaseDatabase db;
    private DatabaseReference reference;

    public CategoriesDao() {
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(Constants.TABLE_CATEGORIES);
    }

    public void getAllCategoriesList(final CategoriesCallback<List<Categories>> callback) {
        new AsyncTask<Void, Void, List<Categories>>() {
            @Override
            protected List<Categories> doInBackground(Void... voids) {
                final List<Categories> categoriesList = new ArrayList<>();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                            String position = childSnapshot.child("position").getValue(String.class);
                            String url = childSnapshot.child("url").getValue(String.class);
                            String title = childSnapshot.child("title").getValue(String.class);
                            String active = childSnapshot.child("active").getValue(String.class);
                            categoriesList.add(new Categories(position, url, title, active));
                        }
                        callback.onSuccess(categoriesList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.toException());
                    }
                });
                return categoriesList;
            }

            @Override
            protected void onPostExecute(List<Categories> categoriesList) {
                super.onPostExecute(categoriesList);
            }
        }.execute();
    }

    public interface CategoriesCallback<T> {
        void onSuccess(T data);
        void onError(Exception e);
    }
}



