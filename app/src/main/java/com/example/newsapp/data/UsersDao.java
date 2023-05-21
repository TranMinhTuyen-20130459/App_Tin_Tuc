package com.example.newsapp.data;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.example.newsapp.AdminActivity;
import com.example.newsapp.models.Users;
import com.example.newsapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UsersDao {
    private FirebaseDatabase db;
    private DatabaseReference reference;

    public UsersDao() {
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(Constants.TABLE_USERS);
    }

    public void getAllUserList(final UserCallback<List<Users>> callback) {
        new AsyncTask<Void, Void, List<Users>>() {
            @Override
            protected List<Users> doInBackground(Void... voids) {
                final List<Users> usersList = new ArrayList<>();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                            String role = childSnapshot.child("role").getValue(String.class);
                            if (role.equals(Constants.ROLE_CUSTOMER)){
                                String username = childSnapshot.child("username").getValue(String.class);
                                String password = childSnapshot.child("password").getValue(String.class);
                                String fullname = childSnapshot.child("fullname").getValue(String.class);
                                usersList.add(new Users(role, username, password, fullname));
                            }
                        }
                        callback.onSuccess(usersList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.toException());
                    }
                });
                return usersList;
            }

            @Override
            protected void onPostExecute(List<Users> usersList) {
                super.onPostExecute(usersList);
            }
        }.execute();
    }

    public interface UserCallback<T> {
        void onSuccess(T data);
        void onError(Exception e);
    }
}
