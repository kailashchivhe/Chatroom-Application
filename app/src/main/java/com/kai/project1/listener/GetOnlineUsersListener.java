package com.kai.project1.listener;

import com.kai.project1.model.OnlineUsers;

import java.util.ArrayList;

public interface GetOnlineUsersListener {
    void allOnlineUsers(ArrayList<OnlineUsers> onlineUsers);
    void allOnlineUsersFailure(String message);
}
