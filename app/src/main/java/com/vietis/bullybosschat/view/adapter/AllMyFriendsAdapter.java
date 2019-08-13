package com.vietis.bullybosschat.view.adapter;

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
import com.vietis.bullybosschat.view.RoomChatActivity;

import java.util.ArrayList;

public class AllMyFriendsAdapter extends RecyclerView.Adapter<AllMyFriendsAdapter.ViewHolder>{
    public static final String IS_FRIEND = "isFriend";
    private Context context;
    private ArrayList<User> users;

    public AllMyFriendsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public AllMyFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_my_friends_item, parent, false);
        return new AllMyFriendsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMyFriendsAdapter.ViewHolder holder, int position) {
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

            tvUsername = itemView.findViewById(R.id.friend_name);
            ivProfile = itemView.findViewById(R.id.friend_image);
        }
    }
}
