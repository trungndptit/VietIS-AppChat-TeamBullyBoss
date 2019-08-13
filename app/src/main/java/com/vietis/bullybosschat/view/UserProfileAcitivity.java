package com.vietis.bullybosschat.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.databinding.UserProfileAcitivityBinding;
import com.vietis.bullybosschat.utils.Actions;
import com.vietis.bullybosschat.utils.GetIn4;
import com.vietis.bullybosschat.view.adapter.AddUserAdapter;
import com.vietis.bullybosschat.view.adapter.AllMyFriendsAdapter;
import com.vietis.bullybosschat.model.User;
import com.vietis.bullybosschat.viewmodel.ProfileActivityViewModel;

import java.util.ArrayList;

public class UserProfileAcitivity extends AppCompatActivity {

    UserProfileAcitivityBinding userProfileAcitivityBinding;

    private ImageView ivProfile;
    private TextView tvUsername, tvAddFriend, tvFriendNumber, tvFollowNumber, tvFollow;
    private ImageView ivAddFriend, ivFollow;

    private ArrayList<String> follows = new ArrayList<>();

    Intent intent;

    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_profile_acitivity);
        userProfileAcitivityBinding = DataBindingUtil.setContentView(this, R.layout.user_profile_acitivity);

        setInit();
        intent = getIntent();
        final String userID = intent.getStringExtra(AddUserAdapter.USER_ID);
        final String isFirend = intent.getStringExtra(AllMyFriendsAdapter.IS_FRIEND);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ProfileActivityViewModel profileActivityViewModel = new ProfileActivityViewModel(false, false);
        userProfileAcitivityBinding.setProfileviewmodel(profileActivityViewModel);
        userProfileAcitivityBinding.getProfileviewmodel().getProfile(userID);
//        userProfileAcitivityBinding.setImageUrl(profileActivityViewModel.getUrlImg());
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