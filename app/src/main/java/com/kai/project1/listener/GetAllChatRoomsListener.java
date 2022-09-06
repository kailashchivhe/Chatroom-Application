package com.kai.project1.listener;

import com.kai.project1.model.ChatRoom;

import java.util.ArrayList;

public interface GetAllChatRoomsListener {
    void allChatRooms(ArrayList<ChatRoom> chatRoomArrayList);
    void allChatRoomsFailure(String message);
}
