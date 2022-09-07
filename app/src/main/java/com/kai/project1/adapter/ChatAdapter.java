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
import com.kai.project1.listener.ProfileDisplayListener;
import com.kai.project1.model.Message;
import com.kai.project1.utils.FirebaseHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> implements DeleteMessageListener, ProfileDisplayListener {

    List<Message> messageList;
    String mChatRoomID;
    AlertDialog.Builder builder;
    ChatHolder holder;

    public ChatAdapter(List<Message> messageList, String mChatRoomID) {
        this.messageList = messageList;
        this.mChatRoomID = mChatRoomID;
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
//                FirebaseHelper.likeMessage();
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
        FirebaseHelper.deleteMessage(mChatRoomID,messageID,this);
        //can adapter implements listener
    }

    @Override
    public void messageDeleted() {
        builder = new AlertDialog.Builder(builder.getContext());
        builder.setTitle("Deleted");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setMessage("Message Deleted");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void messageDeletedFailure(String message) {
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

    @Override
    public void profileDisplay(String uri) {
        Picasso.get().load(uri).into(holder.userImage);
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


