package com.kai.project1.model;

import java.util.ArrayList;

public class ChatRoom {
    ArrayList<Message> messageArrayList;
    String info;
    ArrayList<OnlineUser> userArrayList;

    public ArrayList<OnlineUser> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<OnlineUser> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
