package com.vietis.bullybosschat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class ChooseFriendFragment extends Fragment {
    TextView mTextSearch;
    private Toolbar mToolbar;
    private ImageView mImageAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_friends_fragment, container, false);
        mTextSearch = view.findViewById(R.id.text_search);
        mToolbar =  view.findViewById(R.id.choose_friend_toolbar);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        initToolbar();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Online Member");
        return view;
    }

    private void initToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        Glide.with(getActivity())
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtk3oX9Z6oUrvf5Lb4qWr5w4GWlAsX5P3w6Y_FIrdH6YHL7Sme")
                .circleCrop()
                .into(mImageAvatar);
    }
}
