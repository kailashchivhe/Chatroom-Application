package com.kai.project1.listener;

public interface CreateChatRoomListener {
    void chatRoomCreated();
    void chatRoomCreatedFailure(String message);
}
