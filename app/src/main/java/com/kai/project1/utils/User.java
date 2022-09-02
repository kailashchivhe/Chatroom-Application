package com.kai.project1.utils;

import android.graphics.Bitmap;

public class User {
    String firstname,lastname,userid,gender,city;

    public User(String firstname, String lastname, String gender, String city, String userid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.userid = userid;
        this.gender = gender;
        this.city = city;
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
}
