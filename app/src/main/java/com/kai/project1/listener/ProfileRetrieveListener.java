package com.kai.project1.listener;

import com.kai.project1.model.User;

public interface ProfileRetrieveListener {
    void onSuccess(User user);
    void onFail( String message );
}
