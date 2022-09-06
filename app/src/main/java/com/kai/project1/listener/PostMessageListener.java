package com.kai.project1.listener;

public interface PostMessageListener {
    void messagePosted();
    void messagePostedFailure(String message);
}
