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
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class OnlineFriendAdapter extends RecyclerView.Adapter<OnlineFriendAdapter.ViewHolder>{
    private Context context;
    private ArrayList<User> users;

    public OnlineFriendAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.online_friend_item, parent, false);
        return new OnlineFriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());
        if (user.getImageurl().equals("default")) {
            holder.ivProfile.setImageResource(R.drawable.ic_avatar);
        } else {
            Glide.with(context).load(user.getImageurl()).into(holder.ivProfile);
        }

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

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.member_name);
            ivProfile = itemView.findViewById(R.id.member_image);
        }
    }
}
