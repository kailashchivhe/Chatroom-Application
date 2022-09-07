package com.kai.project1.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.project1.R;
import com.kai.project1.listener.ProfileDisplayListener;
import com.kai.project1.model.OnlineUsers;
import com.kai.project1.utils.FirebaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OnlineUsersAdapter extends RecyclerView.Adapter<OnlineUsersHolder> implements ProfileDisplayListener {

    List<OnlineUsers> onlineUserList;
    OnlineUsersHolder holder;
    AlertDialog.Builder builder;
    public OnlineUsersAdapter(List<OnlineUsers> onlineUserList) {
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
        this.holder = holder;
        OnlineUsers onlineUser = onlineUserList.get(position);
        holder.textView.setText(onlineUser.getUserName().split(" ")[0]);
        FirebaseHelper.getUserProfileImage(onlineUser.getId(),this);
    }

    @Override
    public int getItemCount() {
        return onlineUserList.size();
    }

    @Override
    public void profileDisplay(String uri) {
        Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher_round).into(holder.imageView);
    }

    @Override
    public void profileDisplayFailure(String message) {
        builder = new AlertDialog.Builder(builder.getContext());
        builder.setTitle("Alert!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
