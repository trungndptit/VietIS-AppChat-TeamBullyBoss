package com.vietis.bullybosschat.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class Actions {

    private User user;
//    private ArrayList<String> myFriendsId;
    private static Actions instance;

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

//    public ArrayList<String> getMyFriendsId() {
//        return myFriendsId;
//    }

//    public void setMyFriendsId(ArrayList<String> myFriendsId) {
//        this.myFriendsId = myFriendsId;
//    }

    private Actions() {
        getUserSingleton();
    }
    public static Actions getInstance() {
        if(instance == null) {
            synchronized(Actions.class) {
                if(null == instance) {
                    instance  = new Actions();
                }
            }
        }
        return instance;
    }

    private void getUserSingleton(){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(fuser.getUid())) {
                    setUser(user);
//                    setMyFriendsId(user.getFriends());
                    System.out.println("Debug userID Singleton " + user.getId());
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

    public String getImgUrl(){
        return getUser().getImageurl();
    }

    public ArrayList<String> getMyFriendsId() {
        return getUser().getFriends();
    }

    public static synchronized void destroyInstance(){
        instance = null;
    }

}
