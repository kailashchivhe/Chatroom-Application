package com.kai.project1.model;

import java.io.Serializable;
import java.util.Map;

public class Message implements Serializable {
    Map<String, Boolean> likes;
    String message;
    String date;
    String userId;
    String userName;

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "likes=" + likes +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
