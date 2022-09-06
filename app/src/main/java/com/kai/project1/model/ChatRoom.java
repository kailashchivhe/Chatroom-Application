package com.kai.project1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoom implements Serializable {
    ArrayList<Message> messages;
    String name;
    HashMap<String, String> online;
    String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getOnline() {
        return online;
    }

    public void setOnline(HashMap<String, String> online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "messages=" + messages +
                ", name='" + name + '\'' +
                ", online=" + online +
                '}';
    }
}
