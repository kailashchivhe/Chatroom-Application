package com.kai.project1.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.project1.R;
import com.kai.project1.model.ChatRoom;

import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsHolder>{

    List<ChatRoom> chatRoomList;
    public ChatRoomsAdapter(List<ChatRoom> chatRoomsList) {
        this.chatRoomList = chatRoomList;
    }

    @NonNull
    @Override
    public ChatRoomsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room,parent,false);
        ChatRoomsHolder holder = new ChatRoomsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomsHolder holder, int position) {
        ChatRoom chatRoom = chatRoomList.get(position);
        holder.chatRoomName.setText(chatRoom.getName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                //requires fragment for navigation
//                NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_chat_roomFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }
}
class ChatRoomsHolder extends RecyclerView.ViewHolder{
    TextView chatRoomName;
    View view;
    public ChatRoomsHolder(@NonNull View itemView) {
        super(itemView);
        chatRoomName = itemView.findViewById(R.id.chatRoomName);
        view = itemView;
    }
}
