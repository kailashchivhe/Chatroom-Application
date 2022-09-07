package com.kai.project1.listener;

public interface CreateChatRoomListener {
    void chatRoomCreated(String chatRoomId);
    void chatRoomCreatedFailure(String message);
}
