package com.kai.project1.ui.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
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
import com.kai.project1.listener.DeleteMessageListener;
import com.kai.project1.listener.GetAllMessagesListener;
import com.kai.project1.listener.GetOnlineUsersListener;
import com.kai.project1.listener.PostMessageListener;
import com.kai.project1.listener.RemoveOnlineUserListener;
import com.kai.project1.model.ChatRoom;
import com.kai.project1.model.Message;
import com.kai.project1.model.OnlineUsers;
import com.kai.project1.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragment extends Fragment implements GetOnlineUsersListener, GetAllMessagesListener, AddOnlineUserListener, RemoveOnlineUserListener, PostMessageListener, DeleteMessageListener {

    FragmentChatRoomBinding binding;
    List<Message> messageList = new ArrayList<Message>();;
    List<OnlineUsers> onlineUserList = new ArrayList<OnlineUsers>();;
    AlertDialog.Builder builder;
    private String mChatRoomID;
    OnlineUsersAdapter onlineUsersAdapter;
    ChatAdapter chatAdapter;

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
            mChatRoomID = getArguments().getString("chatId", "");
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
        FirebaseHelper.addOnlineUser(mChatRoomID,this);
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        onlineUsersAdapter = new OnlineUsersAdapter(onlineUserList);
        chatAdapter = new ChatAdapter(messageList,mChatRoomID, this);
        FirebaseHelper.getOnlineUsers(mChatRoomID,this);

        binding.recyclerViewOnline.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.recyclerViewOnline.setAdapter(onlineUsersAdapter);

        FirebaseHelper.getChatRoomMessages(mChatRoomID,this);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMessages.setAdapter(chatAdapter);

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextMessage.getText().toString();
                binding.editTextMessage.setText("");
                onSendClicked(message);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseHelper.removeOnlineUser(mChatRoomID,this);
    }

    void onSendClicked(String message){
        FirebaseHelper.postMessage(mChatRoomID, message, this);
    }
    @Override
    public void allOnlineUsers(ArrayList<OnlineUsers> onlineUsers) {
        onlineUserList.clear();
        onlineUserList.addAll(onlineUsers);
        onlineUsersAdapter.notifyDataSetChanged();
    }

    @Override
    public void allOnlineUsersFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void allMessages(ArrayList<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void allMessagesFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void addOnlineUser() {
//        onlineUserList.clear();
//        onlineUsersAdapter.notifyDataSetChanged();
    }

    @Override
    public void addOnlineUserFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void removeOnlineUser() {
//        onlineUsersAdapter.notifyDataSetChanged();
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
//        messageList.clear();
//        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void messagePostedFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void messageDeleted() {
        builder = new AlertDialog.Builder(getContext());
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
        builder = new AlertDialog.Builder(getContext());
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