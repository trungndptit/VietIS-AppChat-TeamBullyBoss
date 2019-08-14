package com.vietis.bullybosschat.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.adapter.WhomChatAdapter;
import com.vietis.bullybosschat.model.Message;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatFragment extends Fragment {


    RecyclerView rvWhomChat;
    private ArrayList<User> users;

    private ArrayList<String> userList;

    private WhomChatAdapter whomChatAdapter;
    ImageView mImageAvatar;
    EditText mTextSearch;

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


        mData.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);

                    if (message.getSender().equals(fuser.getUid())) {
                        if (checkUserID(userList, message.getReceiver())) {
                            userList.add(message.getReceiver());
                        }
                    }
                    if (message.getReceiver().equals(fuser.getUid())) {
                        if (checkUserID(userList, message.getSender())) {
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

        mTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        initToolbar();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chat");
        return view;
    }

    private boolean checkUserID(ArrayList<String> mIDs, String id) {
        if (mIDs.size() == 0) {
            return true;
        } else {
            for (String mid : mIDs) {
                if (mid.equals(id)) {
                    return false;
                }
            }
            return true;
        }
    }

    private void searchUsers(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (!user.getId().equals(fuser.getUid())){
                        for (String id : userList) {
                            if (user.getId().equals(id)) {
                                users.add(user);
                            }
                        }
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

    private void loadChat() {
        users = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    for (String id : userList) {

                        if (user.getId().equals(id)) {
                            users.add(user);
                        }
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

    private void setInit(View view) {
        rvWhomChat = view.findViewById(R.id.rv_whom_chat);
        mImageAvatar = view.findViewById(R.id.image_avatar);
        mTextSearch = view.findViewById(R.id.text_search);
    }

    private void state(String state){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("state", state);
        mData.child("Users").child(fuser.getUid()).updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        state("onl");
    }

    @Override
    public void onPause() {
        super.onPause();
        state("off");
    }
}
