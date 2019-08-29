package com.vietis.bullybosschat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.vietis.bullybosschat.adapter.RoomChatAdapter;
import com.vietis.bullybosschat.notifications.APIService;
import com.vietis.bullybosschat.fragments.HomeChatActivity;
import com.vietis.bullybosschat.model.Message;
import com.vietis.bullybosschat.model.User;
import com.vietis.bullybosschat.notifications.Client;
import com.vietis.bullybosschat.notifications.Data;
import com.vietis.bullybosschat.notifications.Response;
import com.vietis.bullybosschat.notifications.Sender;
import com.vietis.bullybosschat.notifications.Token;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class RoomChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int IMAGE_CHOOSE  = 1;

    ImageView ivUserAvatar, ivBack;
    TextView tvUsername;
    EditText etChatText;
    ImageButton ibSend;
    ImageButton ibSendImage;
    RecyclerView rvChatTextContainer;

    RoomChatAdapter roomChatAdapter;
    ArrayList<Message> messages;

    FirebaseUser fuser;
    DatabaseReference mData;

    private StorageTask mUpLoadTask;
    private Uri mUrl;
    private boolean isUpdateAvatar = true;

    private StorageReference mStorageReference;

    Intent intent;

    String myid;
    String userid;

    APIService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        setInit();

        apiService = Client.getClient("http://fcm.googleapis.com/").create(APIService.class);

        rvChatTextContainer.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChatTextContainer.setLayoutManager(linearLayoutManager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        myid = fuser.getUid();

        intent = getIntent();
        userid = intent.getStringExtra("userID");

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = etChatText.getText().toString();
                if (!msg.equals("")){
                    sendMessage(myid, userid, msg, "text");
                } else {

                }
                etChatText.setText("");
            }
        });

        ibSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                isUpdateAvatar = true;
            }
        });

        ivBack.setOnClickListener(this);
        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roomChat = new Intent(RoomChatActivity.this, UserProfileAcitivity.class);
                roomChat.putExtra("userID", userid);
                roomChat.putExtra("isFriend", "false");
                startActivity(roomChat);
            }
        });

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

    private void sendMessage(String sender, final String receiver, String message, String type){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());

        mData = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time", currentDateandTime);
        hashMap.put("type", type);

        mData.child("Chats").push().setValue(hashMap);

        final String msg = message;
        mData.child("Users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify){
                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message", userid);
                    Sender sender = new Sender(data, token.getToken());

                    System.out.println("Debug: Retrofit : " );

                    apiService.sendNotification(sender).enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response response) {
                            if (response.code() == 200){
                                if (response.body().success != 1){
                                    Toast.makeText(RoomChatActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setInit() {
        ivUserAvatar = findViewById(R.id.iv_avatar_room_chat);
        ivBack = findViewById(R.id.iv_back_from_room_chat);
        tvUsername = findViewById(R.id.tv_username_room_chat);
        etChatText = findViewById(R.id.et_roomchat_text);
        ibSend = findViewById(R.id.ib_send_chat);
        ibSendImage = findViewById(R.id.ib_send_image);
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


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo for  profile"), IMAGE_CHOOSE);

    }

    private String getFileImage(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void upLoadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (mUrl != null) {
            final StorageReference storageFile = mStorageReference.child(System.currentTimeMillis() + "." + getFileImage(mUrl));
            mUpLoadTask = storageFile.putFile(mUrl);
            mUpLoadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
//                        Toast.makeText(getContext(), "upload image fail", Toast.LENGTH_SHORT).show();

                    }
                    return storageFile.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri urlDowlaod = (Uri) task.getResult();
                        String mUriDowload = urlDowlaod.toString();
                        if (isUpdateAvatar){
                            sendMessage(myid, userid, mUriDowload, "image");
                        }
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(RoomChatActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RoomChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "Not Select Image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CHOOSE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mUrl = data.getData();

            if (mUpLoadTask != null && mUpLoadTask.isInProgress()) {
                Toast.makeText(this, "image  is uploading", Toast.LENGTH_SHORT).show();
            } else {
                upLoadImage();

            }
        }

    }
}