package com.kai.project1.ui.chat;

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
import com.kai.project1.model.Message;
import com.kai.project1.model.OnlineUser;
import com.kai.project1.utils.FirebaseHelper;

import java.util.List;

public class ChatRoomFragment extends Fragment {

    FragmentChatRoomBinding binding;
    private String mParam1;
    private String mParam2;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    public static ChatRoomFragment newInstance(String param1, String param2) {
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

        String chatRoomId = null;
        List<OnlineUser> onlineUserList = FirebaseHelper.getOnlineUsers(chatRoomId);
        List<Message> messagesList = FirebaseHelper.getAllChatRoomMessage(chatRoomId);
        binding.recyclerViewOnline.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.recyclerViewOnline.setAdapter(new OnlineUsersAdapter(onlineUserList));

        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewMessages.setAdapter(new ChatAdapter(messagesList));

    }
}