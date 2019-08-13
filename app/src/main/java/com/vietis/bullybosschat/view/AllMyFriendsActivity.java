package com.vietis.bullybosschat.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.view.adapter.AllMyFriendsAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class AllMyFriendsActivity extends AppCompatActivity {
    private ImageView mImgBack;
    private RecyclerView rvListFriends;
    private AllMyFriendsAdapter allMyFriendsAdapter;
    private ArrayList<User> mUsers;
    private ArrayList<String> friendsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_my_friends);

        initView();

        rvListFriends.setHasFixedSize(true);
        rvListFriends.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUsers = new ArrayList<>();

        getFriendId();
        readUser();


        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });
    }

    private void getFriendId() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        friendsId = new ArrayList<>();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(fuser.getUid())) {
                    if (user.getFriends() == null){
                        friendsId = new ArrayList<>();
                    } else {
                        friendsId = user.getFriends();
                    }

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
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (!user.getId().equals(fuser.getUid())) {
                    for (String id : friendsId) {
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }

                    allMyFriendsAdapter = new AllMyFriendsAdapter(getApplicationContext(), mUsers);
                    rvListFriends.setAdapter(allMyFriendsAdapter);
                    allMyFriendsAdapter.notifyDataSetChanged();
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

    private void initView() {
        mImgBack = findViewById(R.id.image_view_back);
        rvListFriends = findViewById(R.id.list_friends);
    }
}