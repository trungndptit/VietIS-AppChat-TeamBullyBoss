package com.vietis.bullybosschat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.model.User;

public class ProfileFragment extends Fragment {

    private ImageView mImageAvatar;
    private ImageView mImageCover;
    private ImageView mUpdateAvatar;
    private ImageView mUpdateCover;
    private TextView mTextName;
    private TextView mTextOne;
    private TextView mTextTow;
    private TextView mTextFriend;
    private TextView mTextFollow;
    private DatabaseReference reference;

    private FirebaseUser user;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
//        mImageCover=  view.findViewById(R.id.image_background);
//        mUpdateAvatar=  view.findViewById(R.id.image_edit_avatar);
//        mUpdateCover=  view.findViewById(R.id.image_edit_cover);
//        mTextOne=  view.findViewById(R.id.text_one);
//        mTextTow=  view.findViewById(R.id.text_two);
//        mTextFriend=  view.findViewById(R.id.text_friend);
//        mTextFollow=  view.findViewById(R.id.text_follow);


        mImageAvatar=  view.findViewById(R.id.image_avatar);
        mTextName=  view.findViewById(R.id.txt_name);

        user = FirebaseAuth.getInstance().getCurrentUser();

        final   String idUser =  user.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("Debug: addChild profile" );
                User user = dataSnapshot.getValue(User.class);
                if(idUser.equals(user.getId())){
                    mTextName.setText(user.getUsername());
//                    if (user.getImageurl().equals("default")) {
//                        mImageAvatar.setImageResource(R.drawable.anh1);
//                    } else {
//                        Glide.with(getActivity())
//                                .load(user.getImageurl())
//                                .circleCrop()
//                                .into(mImageAvatar);
//                    }
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


//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                mTextName.setText(user.getUsername());

//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return view;

    }




}
