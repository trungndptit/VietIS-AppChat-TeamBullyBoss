package com.vietis.bullybosschat.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.model.User;

public class GetIn4 {
    User mUser;

    public GetIn4() {
    }

    private User getUserByID(final String id){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(id)){
                    mUser = user;
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
        return mUser;
    }

    public String getUserImgUrlByID(String id){
        mUser = getUserByID(id);
        return mUser.getImageurl();
    }
}
