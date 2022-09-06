package com.kai.project1.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.project1.R;
import com.kai.project1.model.OnlineUser;

import java.util.List;

public class OnlineUsersAdapter extends RecyclerView.Adapter<OnlineUsersHolder>{

    List<OnlineUser> onlineUserList;

    public OnlineUsersAdapter(List<OnlineUser> onlineUserList) {
        this.onlineUserList = onlineUserList;
    }

    @NonNull
    @Override
    public OnlineUsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_online,parent,false);
        OnlineUsersHolder holder = new OnlineUsersHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineUsersHolder holder, int position) {
        OnlineUser onlineUser = onlineUserList.get(position);
        holder.textView.setText(onlineUser.getName());
//        holder.imageView.setImageBitmap(onlineUser.getImageUrl());
//        Url to bitmap
    }

    @Override
    public int getItemCount() {
        return onlineUserList.size();
    }
}
class OnlineUsersHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;
    public OnlineUsersHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.textView);
    }
}
