package com.kai.project1.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsHolder>{

    @NonNull
    @Override
    public ChatRoomsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view =LayoutInflater.from(getContext()).inflate()
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class ChatRoomsHolder extends RecyclerView.ViewHolder{

    public ChatRoomsHolder(@NonNull View itemView) {
        super(itemView);
    }
}
