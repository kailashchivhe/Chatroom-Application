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
import com.kai.project1.listener.DeleteMessageListener;
import com.kai.project1.listener.LikeListener;
import com.kai.project1.listener.ProfileDisplayListener;
import com.kai.project1.model.Message;
import com.kai.project1.utils.FirebaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> implements ProfileDisplayListener, LikeListener {

    List<Message> messageList;
    String mChatRoomID;
    AlertDialog.Builder builder;
    ChatHolder holder;
    DeleteMessageListener deleteMessageListener;

    public ChatAdapter(List<Message> messageList, String mChatRoomID, DeleteMessageListener deleteMessageListener) {
        this.messageList = messageList;
        this.mChatRoomID = mChatRoomID;
        this.deleteMessageListener = deleteMessageListener;
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
        this.holder = holder;
        Message message = messageList.get(position);
        holder.message.setText(message.getMessage());
        FirebaseHelper.getUserProfileImage(message.getUserId(),this);
        holder.name.setText(message.getUserName());
        holder.time.setText(message.getDate());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do like or Unlike
            }
        });
        if(message.getLikes().containsKey(FirebaseHelper.getUser().getUid())){
            holder.like.setImageResource(R.drawable.like_favorite);
        }
        else{
            holder.like.setImageResource(R.drawable.like_not_favorite);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.likeMessage(message.getMessageId(), mChatRoomID, message.getLikes().containsKey(FirebaseHelper.getUser().getUid()) );
            }
        });

        if(!message.getUserId().contains(FirebaseHelper.getUser().getUid())){
            holder.delete.setVisibility(View.INVISIBLE);
        }
        else{
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(message.getMessageId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    void onDeleteClicked(String messageID){
        FirebaseHelper.deleteMessage(mChatRoomID,messageID,deleteMessageListener);
        //can adapter implements listener
    }

    @Override
    public void profileDisplay(String uri) {
        Picasso.get().load(uri).placeholder(R.mipmap.ic_launcher_round).into(holder.userImage);
    }

    @Override
    public void profileDisplayFailure(String message) {
//        builder = new AlertDialog.Builder(builder.getContext());
//        builder.setTitle("Alert!");
//        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.setMessage(message);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
    }

    @Override
    public void onSuccess() {
        notifyDataSetChanged();
    }

    @Override
    public void onFailure(Exception exception) {
        notifyDataSetChanged();
    }
}

class ChatHolder extends RecyclerView.ViewHolder{

    ImageView userImage;
    ImageView delete;
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
        delete = itemView.findViewById(R.id.imageViewDelete);
        view = itemView;
    }
}


