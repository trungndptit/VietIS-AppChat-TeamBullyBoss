package com.vietis.bullybosschat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.RoomChatActivity;
import com.vietis.bullybosschat.UserProfileAcitivity;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.ViewHolder>{

    private Context context;
    private ArrayList<User> users;
    int size = 9;
    private User mUser;

    DatabaseReference mData;
    FirebaseUser fuser;

    public FriendsRequestAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_item, parent, false);
        return new FriendsRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mData = FirebaseDatabase.getInstance().getReference();
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());
        if (user.getImageurl().equals("default")) {
            holder.ivProfile.setImageResource(R.drawable.ic_avatar);
        } else {
            Glide.with(context).load(user.getImageurl()).into(holder.ivProfile);
        }
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriendToMyData(fuser.getUid(), user.getId());
                addFriendToMyData(user.getId(), fuser.getUid());
//                addFriendToMyData(getUser(fuser.getUid()), user.getId());
//                mUser = getUser(fuser.getUid());
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnDeny.setText("Accepted");
                holder.btnDeny.setClickable(false);
            }
        });

        holder.btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roomChat = new Intent(context, UserProfileAcitivity.class);
                roomChat.putExtra("userID", user.getId());
                context.startActivity(roomChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void addFriendToMyData(final String myId, final String userId){
//        mData.child("Users").child(myId).child("friends").push().setValue(userId);


        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(myId)){
                    size = user.getFriends().size();
                    ArrayList<String> friendsList = user.getFriends();
                    friendsList.add(userId);
                    putData(myId, friendsList);
                    if (fuser.getUid().equals(myId)){
                        deleteRequest(userId, myId);
                        System.out.println("Debug addFriendToMyData: "+userId+myId);
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




//        System.out.println("Debug addFriendToMyData");
//        ArrayList<String> friendsId = myUser.getFriends();
////        friendsId.add(userId);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put(friendsId.size() + "", userId);
//        mData.child("Users").child(myUser.getId()).child("friends").setValue(hashMap);
    }

    private void putData(String id, ArrayList<String> friendsList){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("friends", friendsList);
        mData.child("Users").child(id).updateChildren(hashMap);
    }

    private void deleteRequest(String sender, String target){
        mData.child("friendRequest").child(sender + target).removeValue();
    }

    private User getUser(final String myId){
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("Debug getUser user.getId(): " + user.getId());
                System.out.println("Debug getUser myId: " + myId);
                if (user.getId().equals(myId)){
                    mUser = user;
                    System.out.println("Debug getUser: " + mUser.getId());
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
        return mUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public ImageView ivProfile;
        public Button btnAccept, btnDeny;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.friend_name_request);
            ivProfile = itemView.findViewById(R.id.friend_image_request);
            btnAccept = itemView.findViewById(R.id.request_accept);
            btnDeny = itemView.findViewById(R.id.request_deny);
        }
    }
}
