package com.kai.project1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kai.project1.utils.FirebaseHelper;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
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
        binding.chatRoomsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatRoomsList.setAdapter(new ChatRoomsAdapter());
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
                FirebaseHelper.createChatRoom(chatRoomName);
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


}