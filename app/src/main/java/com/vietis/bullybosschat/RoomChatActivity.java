package com.vietis.bullybosschat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.ValueEventListener;
import com.vietis.bullybosschat.adapter.RoomChatAdapter;
import com.vietis.bullybosschat.fragments.HomeChatActivity;
import com.vietis.bullybosschat.model.Message;
import com.vietis.bullybosschat.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RoomChatActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivUserAvatar, ivBack;
    TextView tvUsername;
    EditText etChatText;
    ImageButton ibSend;
    RecyclerView rvChatTextContainer;

    RoomChatAdapter roomChatAdapter;
    ArrayList<Message> messages;

    FirebaseUser fuser;
    DatabaseReference mData;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        setInit();
        rvChatTextContainer.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChatTextContainer.setLayoutManager(linearLayoutManager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        final String myid = fuser.getUid();

        intent = getIntent();
        final String userid = intent.getStringExtra("userID");

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etChatText.getText().toString();
                if (!msg.equals("")){
                    sendMessage(myid, userid, msg);
                } else {

                }
                etChatText.setText("");
            }
        });

        ivBack.setOnClickListener(this);

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(userid)){
                    tvUsername.setText(user.getUsername());
                    if (user.getImageurl().equals("default")){
                        ivUserAvatar.setImageResource(R.drawable.ic_avatar);
                    } else {
                        Glide.with(RoomChatActivity.this).load(user.getImageurl()).into(ivUserAvatar);
                    }
                    readMessage(myid, userid, user.getImageurl());
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

    private void readMessage(final String myid, final String userid, final String imageURL){
        messages = new ArrayList<>();

        mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Message message = ds.getValue(Message.class);
                    assert message != null;
                    if (message.getSender().equals(myid) && message.getReceiver().equals(userid)
                            || message.getSender().equals(userid) && message.getReceiver().equals(myid)){
                        messages.add(message);
                    }

                    roomChatAdapter = new RoomChatAdapter(RoomChatActivity.this, messages, imageURL);
                    rvChatTextContainer.setAdapter(roomChatAdapter);
                    roomChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());

        mData = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time", currentDateandTime);

        mData.child("Chats").push().setValue(hashMap);
    }

    private void setInit() {
        ivUserAvatar = findViewById(R.id.iv_avatar_room_chat);
        ivBack = findViewById(R.id.iv_back_from_room_chat);
        tvUsername = findViewById(R.id.tv_username_room_chat);
        etChatText = findViewById(R.id.et_roomchat_text);
        ibSend = findViewById(R.id.ib_send_chat);
        rvChatTextContainer = findViewById(R.id.rv_room_chat_container);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_from_room_chat:
                Intent chatIntent = new Intent(this, HomeChatActivity.class);
                startActivity(chatIntent);
        }
    }

    private void state(String state){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("state", state);
        mData.child("Users").child(fuser.getUid()).updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        state("onl");
    }

    @Override
    protected void onPause() {
        super.onPause();
        state("off");
    }
}