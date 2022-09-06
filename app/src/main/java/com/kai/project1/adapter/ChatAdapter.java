package com.kai.project1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.project1.R;
import com.kai.project1.model.Message;
import com.kai.project1.model.OnlineUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder>{

    List<Message> messageList;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);
        ChatHolder holder = new ChatHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

class ChatHolder extends RecyclerView.ViewHolder{

    ImageView userImage;
    ImageView like;
    TextView message;
    TextView name;
    TextView time;
    View view;
    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.UserImageView);
        like = itemView.findViewById(R.id.LikeImageView);
        message = itemView.findViewById(R.id.Message);
        name = itemView.findViewById(R.id.textViewName);
        time = itemView.findViewById(R.id.textViewTime);
        view = itemView;
    }
}


