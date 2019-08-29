package com.vietis.bullybosschat.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.fragments.ChatFragment;
import com.vietis.bullybosschat.fragments.OnlineFriendFragment;
import com.vietis.bullybosschat.fragments.ProfileFragment;
import com.vietis.bullybosschat.notifications.Token;

public class HomeChatActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chat);
        mBottomNavi = findViewById(R.id.view_bottom_navigation);
        loadFragment(new ChatFragment());
        initBottonNavi();

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Token mToken = new Token(token);
        assert fuser != null;
        ref.child("Tokens").child(fuser.getUid()).setValue(mToken);
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_fragment, fragment);
        transaction.commit();
    }

    private void initBottonNavi() {
        mBottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.menu_chat:
                        fragment =  new ChatFragment();
                        loadFragment( fragment);
                        return true;
                    case R.id.menu_friends:
                        fragment =  new OnlineFriendFragment();
                        loadFragment( fragment);
                        return true;
                    case R.id.menu_profile:
                        fragment =  new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        });
    }
}
