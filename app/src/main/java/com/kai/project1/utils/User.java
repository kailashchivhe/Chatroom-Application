package com.kai.project1.utils;

import android.graphics.Bitmap;

public class User {
    String firstname,lastname,userid,uri,gender,city,email,password;
    Bitmap bitmapProfile;

    public User(String firstname, String lastname, String uri, String gender, String city, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.userid = userid;
//        this.uri = uri;
        this.gender = gender;
        this.city = city;
        this.email = email;
        this.password = password;
//        this.bitmapProfile = bitmapProfile;
    }

    public Bitmap getBitmapProfile() {
        return bitmapProfile;
    }

    public void setBitmapProfile(Bitmap bitmapProfile) {
        this.bitmapProfile = bitmapProfile;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
