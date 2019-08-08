package com.vietis.bullybosschat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.AddUsersActivity;
import com.vietis.bullybosschat.AllMyFriendsActivity;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.adapter.OnlineFriendAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class OnlineFriendFragment extends Fragment {
    private EditText mTextSearch;
    private ImageView mImageAddFriend;
    private ImageView mImageAllFriend;

    private RecyclerView rvListOnline;
    private OnlineFriendAdapter onlineFriendAdapter;
    private ArrayList<User> mUsers;
    private ArrayList<String> friendsId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.online_friend_fragment, container, false);
        setInit(view);
        rvListOnline.setHasFixedSize(true);
        rvListOnline.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();

        getFriendId();
        readUser();

        mImageAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddUsersActivity.class);
                startActivity(intent);
            }
        });

        mImageAllFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllMyFriendsActivity.class);
                startActivity(intent);
            }
        });

        mTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void getFriendId() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user.getId().equals(fuser.getUid())) {
                        friendsId = user.getFriends();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUsers(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (!user.getId().equals(fuser.getUid()) && user.getState().equals("onl")){
                        for (String id : friendsId) {
                            if (user.getId().equals(id)) {
                                mUsers.add(user);
                            }
                        }
                    }
                }
                onlineFriendAdapter = new OnlineFriendAdapter(getContext(), mUsers);
                rvListOnline.setAdapter(onlineFriendAdapter);
                onlineFriendAdapter.notifyDataSetChanged();
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
                if (!user.getId().equals(fuser.getUid()) && user.getState().equals("onl")) {
                    for (String id : friendsId) {
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }

                    onlineFriendAdapter = new OnlineFriendAdapter(getContext(), mUsers);
                    rvListOnline.setAdapter(onlineFriendAdapter);
                    onlineFriendAdapter.notifyDataSetChanged();
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

    private void setInit(View view){
        mTextSearch = view.findViewById(R.id.text_search);
        rvListOnline = view.findViewById(R.id.list_friend_online);
        mImageAddFriend = view.findViewById(R.id.image_add_friend);
        mImageAllFriend = view.findViewById(R.id.image_all_friend);
    }
}

