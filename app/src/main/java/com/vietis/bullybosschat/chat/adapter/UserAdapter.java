package com.vietis.bullybosschat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.chat.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUser;

    public UserAdapter(Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUser.get(position);
        holder.txtName.setText(user.getmName());
        if (user.getmAvatar().equals("default")) {
            holder.imageAvatar.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getmAvatar()).into(holder.imageAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageAvatar;
        public TextView txtName;
        public TextView txtMes;
        public TextView txtTimeSend;

        public ViewHolder(View itemView) {
            super(itemView);

            imageAvatar = itemView.findViewById(R.id.image_avatar);
            txtName = itemView.findViewById(R.id.txt_name);
            txtMes = itemView.findViewById(R.id.text_message);
            txtTimeSend = itemView.findViewById(R.id.txt_time_seen);
        }
    }
}
