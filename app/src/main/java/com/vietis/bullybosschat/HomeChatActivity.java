package com.vietis.bullybosschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeChatActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_chat);
        mBottomNavi = findViewById(R.id.view_bottom_navigation);
        loadFragment(new ChatFragment());
        initBottonNavi();
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_fragment, fragment);
//        transaction.addToBackStack(null);
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
