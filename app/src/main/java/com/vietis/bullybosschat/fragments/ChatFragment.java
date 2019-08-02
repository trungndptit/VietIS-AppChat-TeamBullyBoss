package com.vietis.bullybosschat.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.adapter.WhomChatAdapter;
import com.vietis.bullybosschat.model.Message;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class ChatFragment  extends Fragment {


    RecyclerView rvWhomChat;
    private ArrayList<User> users;

    private ArrayList<String> userList;

    private WhomChatAdapter whomChatAdapter;
    ImageView mImageAvatar;

    FirebaseUser fuser;
    DatabaseReference mData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        users = new ArrayList<>();
        userList = new ArrayList<>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference();
        setInit(view);
        rvWhomChat.setHasFixedSize(true);
        rvWhomChat.setLayoutManager(new LinearLayoutManager(getContext()));
//        Glide.with(getActivity())
//                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtk3oX9Z6oUrvf5Lb4qWr5w4GWlAsX5P3w6Y_FIrdH6YHL7Sme")
//                .circleCrop()
//                .into(mImageAvatar);

        mData.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);

                    if (message.getSender().equals(fuser.getUid())){
                        if (checkUserID(userList, message.getReceiver())){
                            userList.add(message.getReceiver());
                        }
                    }
                    if (message.getReceiver().equals(fuser.getUid())){
                        if (checkUserID(userList, message.getSender())){
                            userList.add(message.getSender());
                        }
                    }
                }

                loadChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        initToolbar();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat");
        return view;
    }

    private boolean checkUserID(ArrayList<String> mIDs, String id){
        if (mIDs.size() == 0){
            return true;
        } else {
            for (String mid : mIDs){
                if (mid.equals(id)){
                    return false;
                }
            }
            return true;
        }
    }

    private void loadChat() {
        users = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);

                    for (String id : userList){

                        if (user.getId().equals(id)){
                            users.add(user);
                        }
//                        if (user.getId().equals(id)){
//                            if (listIterator.hasNext()){
//
//                                while (listIterator.hasNext()){
//                                    User user1 = (User) listIterator.next();
//                                    if (!user.getId().equals(user1.getId())){
//                                        listIterator.add(user);
//                                        users.add(user);
//                                    }
//                                }
//                            } else {
//                                listIterator.add(user);
//                                users.add(user);
//                            }
//                        }
                    }
                }

                whomChatAdapter = new WhomChatAdapter(getContext(), users);
                rvWhomChat.setAdapter(whomChatAdapter);
                whomChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInit(View view){
        rvWhomChat = view.findViewById(R.id.rv_whom_chat);
        mImageAvatar = view.findViewById(R.id.image_avatar);
    }

//    private void initToolbar() {
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(mToolbar);
//        Glide.with(getActivity())
//                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRtk3oX9Z6oUrvf5Lb4qWr5w4GWlAsX5P3w6Y_FIrdH6YHL7Sme")
//                .circleCrop()
//                .into(mImageAvatar);
//
//    }


}