package com.kai.project1.listener;

public interface DeleteMessageListener {
    void messageDeleted();
    void messageDeletedFailure(String message);
}
