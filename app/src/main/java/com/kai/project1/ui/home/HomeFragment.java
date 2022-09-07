package com.kai.project1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kai.project1.R;
import com.kai.project1.adapter.ChatRoomsAdapter;

import com.kai.project1.databinding.FragmentHomeBinding;
import com.kai.project1.listener.CreateChatRoomListener;
import com.kai.project1.listener.GetAllChatRoomsListener;
import com.kai.project1.model.ChatRoom;
import com.kai.project1.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GetAllChatRoomsListener, CreateChatRoomListener {

    FragmentHomeBinding binding;
    AlertDialog.Builder builder;
    List<ChatRoom> chatRoomsList = new ArrayList<ChatRoom>();
    ChatRoomsAdapter chatRoomsAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            onProfileClicked();
            return true;
        }
        else if (id == R.id.action_new_chat) {
            createRoom();
            return true;
        }
        else if (id == R.id.action_logout) {
            onLogoutClicked();
            return true;
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        FirebaseHelper.getAllChatRooms(this);
        binding.chatRoomsList.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRoomsAdapter = new ChatRoomsAdapter(chatRoomsList);
        binding.chatRoomsList.setAdapter(chatRoomsAdapter);
    }

    private void onProfileClicked(){
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_profileFragment);
    }


    private void onLogoutClicked(){
        FirebaseHelper.logout();
        NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_loginFragment);
    }

    private void createRoom(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter chat room name");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("ChatRoom name");
        builder.setView(input);

        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String chatRoomName = input.getText().toString();
                onCreateRoomClicked(chatRoomName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    void onCreateRoomClicked(String chatRoomName){
        FirebaseHelper.createChatRoom(chatRoomName,this);
    }

    @Override
    public void allChatRooms(ArrayList<ChatRoom> chatRoomArrayList) {
        chatRoomsList.clear();
        chatRoomsList.addAll(chatRoomArrayList);
        chatRoomsAdapter.notifyDataSetChanged();
    }

    @Override
    public void allChatRoomsFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void chatRoomCreated(String chatRoomId) {
        //list reload required
        Bundle bundle = new Bundle();
        bundle.putSerializable( "chatId", chatRoomId );
        NavHostFragment.findNavController( this ).navigate( R.id.action_HomeFragment_to_chat_roomFragment, bundle );
//        chatRoomsAdapter.notifyDataSetChanged();
    }

    @Override
    public void chatRoomCreatedFailure(String message) {
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}