package com.vietis.bullybosschat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.adapter.OnlineFriendAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class OnlineFriendFragment extends Fragment {
    TextView mTextSearch;
    private Toolbar mToolbar;
    private ImageView mImageAvatar;

    private ImageButton ibContact, ibAddFriends;
    private RecyclerView rvListOnline;

    private OnlineFriendAdapter onlineUserAdapter;
    private ArrayList<User> users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.online_friend_fragment, container, false);
        setInit(view);
        rvListOnline.setHasFixedSize(true);
        rvListOnline.setLayoutManager(new LinearLayoutManager(getContext()));
        users = new ArrayList<>();

        readUser();

//        initToolbar();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Online Member");
        return view;
    }

    private void readUser() {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Debug: on AddChild");
                User user = dataSnapshot.getValue(User.class);
                if (!user.getId().equals(fuser.getUid()) && user.getState().equals("onl")){
                    users.add(user);
                }

                onlineUserAdapter = new OnlineFriendAdapter(getContext(), users);
                rvListOnline.setAdapter(onlineUserAdapter);
                onlineUserAdapter.notifyDataSetChanged();
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

//    private void initToolbar() {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(mToolbar);
//        Glide.with(getActivity())
//                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtk3oX9Z6oUrvf5Lb4qWr5w4GWlAsX5P3w6Y_FIrdH6YHL7Sme")
//                .circleCrop()
//                .into(mImageAvatar);
//    }

    private void setInit(View view){
        mTextSearch = view.findViewById(R.id.text_search);
        mToolbar =  view.findViewById(R.id.choose_friend_toolbar);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        rvListOnline = view.findViewById(R.id.list_friend_online);
    }
}
