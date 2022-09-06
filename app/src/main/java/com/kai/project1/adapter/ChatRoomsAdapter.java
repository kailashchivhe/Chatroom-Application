package com.kai.project1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.project1.R;
import com.kai.project1.model.ChatRooms;

import java.util.List;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsHolder>{

    List<ChatRooms> chatRoomsList;

    public ChatRoomsAdapter(List<ChatRooms> chatRoomsList) {
        this.chatRoomsList = chatRoomsList;
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

    }

    @Override
    public int getItemCount() {
        return chatRoomsList.size();
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
