package com.vietis.bullybosschat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.adapter.AddUserAdapter;
import com.vietis.bullybosschat.adapter.AllMyFriendsAdapter;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class UserProfileAcitivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView tvUsername, tvAddFriend, tvFriendNumber, tvFollowNumber, tvFollow;
    private ImageView ivAddFriend, ivFollow;

    private ArrayList<String> follows = new ArrayList<>();

    Intent intent;

    DatabaseReference mData;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_acitivity);

        setInit();
        intent = getIntent();
        final String userID = intent.getStringExtra(AddUserAdapter.USER_ID);
        final String isFirend = intent.getStringExtra(AllMyFriendsAdapter.IS_FRIEND);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(userID)){
                    follows = user.getFollows();
                    tvUsername.setText(user.getUsername());
                    tvFriendNumber.setText(String.valueOf(user.getFriends().size()));
                    tvFollowNumber.setText(String.valueOf(user.getFollows().size()-1));
                    if (user.getImageurl().equals("default")) {
                        ivProfile.setImageResource(R.drawable.ic_avatar);
                    } else {
                        Glide.with(UserProfileAcitivity.this).load(user.getImageurl()).into(ivProfile);
                    }
                    if (isFirend.equals("true")){
                        ivAddFriend.setVisibility(View.GONE);
                        tvAddFriend.setText("Friend");
                    }
                    for (String id : follows){
                        if (fuser.getUid().equals(id)){
                            tvFollow.setText("Followed");
                            ivFollow.setVisibility(View.GONE);
                            break;
                        }
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

    private void setInit() {
        ivProfile = findViewById(R.id.image_avatar);
        tvUsername = findViewById(R.id.txt_name);
        tvAddFriend = findViewById(R.id.text_add_friend);
        ivAddFriend = findViewById(R.id.image_add_friend);
        tvFriendNumber = findViewById(R.id.text_friend_number);
        tvFollowNumber = findViewById(R.id.text_follow_number);
        tvFollow = findViewById(R.id.text_follow1);
        ivFollow = findViewById(R.id.image_follow);
    }
}
