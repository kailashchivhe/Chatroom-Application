package com.kai.project1.model;

import java.util.Map;

public class Message {
    Map<String, Boolean> likesMap;
    String message;
    String date;
    String userId;

    public Map<String, Boolean> getLikesMap() {
        return likesMap;
    }

    public void setLikesMap(Map<String, Boolean> likesMap) {
        this.likesMap = likesMap;
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
}
