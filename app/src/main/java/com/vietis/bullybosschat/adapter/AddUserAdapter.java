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
import com.vietis.bullybosschat.UserProfileAcitivity;
import com.vietis.bullybosschat.model.User;

import java.util.ArrayList;

public class AddUserAdapter extends RecyclerView.Adapter<AddUserAdapter.ViewHolder>{
    public static final String USER_ID = "userID";
    private Context context;
    private ArrayList<User> users;

    public AddUserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public AddUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_users_item, parent, false);
        return new AddUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserAdapter.ViewHolder holder, int position) {
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
                Intent userProfile = new Intent(context, UserProfileAcitivity.class);
                userProfile.putExtra(USER_ID, user.getId());
                userProfile.putExtra("isFriend", "false");
                context.startActivity(userProfile);
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
            tvUsername = itemView.findViewById(R.id.add_friend_name);
            ivProfile = itemView.findViewById(R.id.friend_image);
        }
    }
}
