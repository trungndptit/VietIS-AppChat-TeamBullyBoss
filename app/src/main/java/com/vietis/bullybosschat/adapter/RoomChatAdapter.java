package com.vietis.bullybosschat.adapter;

import android.content.Context;
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
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.model.Message;

import java.util.List;

public class RoomChatAdapter extends RecyclerView.Adapter<RoomChatAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Message> messages;
    private String imageURL;

    FirebaseUser fuser;

    public RoomChatAdapter(Context context, List<Message> messages, String imageURL) {
        this.context = context;
        this.messages = messages;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.my_chat_item, parent, false);
            return new RoomChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.their_chat_item, parent, false);
            return new RoomChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RoomChatAdapter.ViewHolder holder, int position) {
        Message message = messages.get(holder.getAdapterPosition());
        System.out.println("Debug Adapter: messages.size-" + messages.size() + " and the position: " + position + " and the getAdapterPosition: " + holder.getAdapterPosition());
        holder.tvMessage.setText(message.getMessage());
        if (imageURL.equals("default")){
            holder.ivProfile.setImageResource(R.drawable.ic_avatar);
        } else {
            Glide.with(context).load(imageURL).into(holder.ivProfile);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;
        public ImageView ivProfile;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_room_chat_text);
            ivProfile = itemView.findViewById(R.id.civ_avatar_room_chat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
