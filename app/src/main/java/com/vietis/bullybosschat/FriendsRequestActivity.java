package com.vietis.bullybosschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.adapter.FriendsRequestAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class FriendsRequestActivity extends AppCompatActivity {
    private ImageView mImgBack;
    private RecyclerView rvListRequest;
    private ArrayList<User> mUsers;
    private FriendsRequestAdapter friendsRequestAdapter;

    ArrayList listRequest;

    DatabaseReference mData;
    FirebaseUser fuser;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request);

        mData = FirebaseDatabase.getInstance().getReference();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        listRequest = intent.getStringArrayListExtra("request");
        System.out.println("Debug getStringArrayListExtra" + listRequest.size());

        initView();

        rvListRequest.setHasFixedSize(true);
        rvListRequest.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mUsers = new ArrayList<>();

        getListUser(listRequest);

    }


    private void getListUser(final ArrayList<String> listRequest){
        System.out.println("Debug mUser.size " + mUsers.size());
        System.out.println("Debug listRequest.size " + listRequest.size());
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                for (String id : listRequest){
                    assert user != null;
                    if (id.equals(user.getId())){
                        mUsers.add(user);
                        friendsRequestAdapter = new FriendsRequestAdapter(getApplicationContext(), mUsers);
                        rvListRequest.setAdapter(friendsRequestAdapter);
                        friendsRequestAdapter.notifyDataSetChanged();
                        System.out.println("Debug mUser.size " + mUsers.size());
                        break;
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

    private void initView() {
        mImgBack = findViewById(R.id.image_view_back_request);
        rvListRequest = findViewById(R.id.rv_friends_request);
    }
}
