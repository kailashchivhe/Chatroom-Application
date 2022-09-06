package com.kai.project1.ui.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kai.project1.R;
import com.kai.project1.adapter.ChatAdapter;
import com.kai.project1.adapter.OnlineUsersAdapter;
import com.kai.project1.databinding.FragmentChatRoomBinding;
import com.kai.project1.databinding.FragmentHomeBinding;
import com.kai.project1.listener.AddOnlineUserListener;
import com.kai.project1.listener.GetAllMessagesListener;
import com.kai.project1.listener.GetOnlineUsersListener;
import com.kai.project1.listener.PostMessageListener;
import com.kai.project1.listener.RemoveOnlineUserListener;
import com.kai.project1.model.Message;
import com.kai.project1.model.OnlineUsers;
import com.kai.project1.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment implements GetOnlineUsersListener, GetAllMessagesListener, AddOnlineUserListener, RemoveOnlineUserListener, PostMessageListener {

    FragmentChatRoomBinding binding;
    List<Message> messageList;
    List<OnlineUsers> onlineUserList;
    AlertDialog.Builder builder;

    private String mChatRoomID;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    public static ChatRoomFragment newInstance(String param1) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        FirebaseHelper.getOnlineUsers(mChatRoomID,this);
//        FirebaseHelper.getAllMessages(mChatRoomID,this);


        binding.recyclerViewOnline.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.recyclerViewOnline.setAdapter(new OnlineUsersAdapter(onlineUserList));

        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMessages.setAdapter(new ChatAdapter(messageList));

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextMessage.getText().toString();
                onSendClicked(message);
            }
        });

    }

    void onSendClicked(String message){
        FirebaseHelper.postMessage(mChatRoomID, message, this);
    }
    @Override
    public void allOnlineUsers(ArrayList<OnlineUsers> onlineUsers) {
        onlineUserList = onlineUsers;
    }

    @Override
    public void allOnlineUsersFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void allMessages(ArrayList<Message> messages) {
        messageList = messages;
    }

    @Override
    public void allMessagesFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void addOnlinerUser() {

    }

    @Override
    public void addOnlineUserFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void removeOnlinerUser() {

    }

    @Override
    public void removeOnlineUserFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void messagePosted() {
        //ToDo after message posted
    }

    @Override
    public void messagePostedFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}