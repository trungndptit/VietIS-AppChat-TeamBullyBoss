package com.vietis.bullybosschat;

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
import com.vietis.bullybosschat.adapter.AllFriendsAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class AllFriendsActivity extends AppCompatActivity {

    RecyclerView rvListUser;
    private AllFriendsAdapter allFriendsAdapter;
    private ArrayList<User> users;
    private ArrayList<String> friendsID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_friends);
        setInit();
        rvListUser.setHasFixedSize(true);
        rvListUser.setLayoutManager(new LinearLayoutManager(this));

        users = new ArrayList<>();
        friendsID = new ArrayList<>();

        readUser();
    }

    private void loadFriend(){
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        final String myid = fuser.getUid();
        friendsID.clear();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(myid)){
                    friendsID = user.getFriends();
                }
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

    private void readUser() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        final String myid = fuser.getUid();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        loadFriend();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                for (String id : friendsID){
                    if (user.getId().equals(id)){
                        users.add(user);
                    }
                }

                allFriendsAdapter = new AllFriendsAdapter(AllFriendsActivity.this, users);
                rvListUser.setAdapter(allFriendsAdapter);
                allFriendsAdapter.notifyDataSetChanged();
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
        rvListUser = findViewById(R.id.rv_all_friends);
    }
}
