package com.vietis.bullybosschat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.adapter.AddUserAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class AddUsersActivity extends AppCompatActivity {

    RecyclerView rvListUser;
    private AddUserAdapter addUserAdapter;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        setInit();
        rvListUser.setHasFixedSize(true);
        rvListUser.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();

        readUser();
    }

    private void readUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String myid = firebaseUser.getUid();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Debug: on AddChild");
                User user = dataSnapshot.getValue(User.class);
                if (!user.getId().equals(myid)){
                    users.add(user);
                }

                addUserAdapter = new AddUserAdapter(AddUsersActivity.this, users);
                rvListUser.setAdapter(addUserAdapter);
                addUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInit() {
        rvListUser = findViewById(R.id.rv_add_users);
    }
}
