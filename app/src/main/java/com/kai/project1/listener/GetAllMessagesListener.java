package com.kai.project1.listener;

import com.kai.project1.model.Message;

import java.util.ArrayList;

public interface GetAllMessagesListener {
    void allMessages(ArrayList<Message> messages);
    void allMessagesFailure(String message);
}
