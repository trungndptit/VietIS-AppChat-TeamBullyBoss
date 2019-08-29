package com.vietis.bullybosschat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.vietis.bullybosschat.RoomChatActivity;
import com.vietis.bullybosschat.model.Message;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class WhomChatAdapter extends RecyclerView.Adapter<WhomChatAdapter.ViewHolder>{
    private Context context;
    private ArrayList<User> users;
    private String thelastmsg;
    private String thelasttime;

    public WhomChatAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new WhomChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());
        if (user.getImageurl().equals("default")) {
            holder.ivProfile.setImageResource(R.drawable.ic_avatar);
        } else {
            Glide.with(context).load(user.getImageurl()).circleCrop().into(holder.ivProfile);
        }


        if (user.getState().equals("onl")) {
            holder.civIsOnl.setVisibility(View.VISIBLE);
        } else {
            holder.civIsOnl.setVisibility(View.GONE);
        }

        theLastMessage(user.getId(), holder.tvLastMsg, holder.txt_time_seen);

        holder.tvLastMsg.setText(thelastmsg);
        holder.txt_time_seen.setText(thelasttime);
        System.out.println("Debug Chat: users = " + users.size());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roomChat = new Intent(context, RoomChatActivity.class);
                roomChat.putExtra("userID", user.getId());
                context.startActivity(roomChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUsername;
        public ImageView ivProfile;
        public TextView tvLastMsg;
        public ImageView civIsOnl;
        public TextView txt_time_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.txt_name);
            ivProfile = itemView.findViewById(R.id.image_avatar);
            tvLastMsg = itemView.findViewById(R.id.text_message);
            civIsOnl = itemView.findViewById(R.id.civ_chat_online);
            txt_time_seen = itemView.findViewById(R.id.txt_time_seen);
        }
    }

    private void theLastMessage(final String userid, final TextView lastmsg, final TextView timemsg) {
        thelastmsg = "default";

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    if (message.getReceiver().equals(fuser.getUid()) && message.getSender().equals(userid)
                            || message.getReceiver().equals(userid) && message.getSender().equals(fuser.getUid())) {
                        if (message.getType().equals("text")){
                            thelastmsg = message.getMessage();
                        } else {
                            thelastmsg = "Đã gửi 1 ảnh";
                        }
                        thelasttime = message.getTime();
                    }
                }

                switch (thelastmsg) {
                    case "default":
                        lastmsg.setText("");
                        break;
                    default:
                        lastmsg.setText(thelastmsg);
                        timemsg.setText(thelasttime);
                        break;
                }
                thelastmsg = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
