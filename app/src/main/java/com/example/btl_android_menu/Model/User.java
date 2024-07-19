package com.example.btl_android_menu.Model;

public class User {
    private String phoneNumber;
    private String gender;
    private String birthday;

    public User() {
        // Constructor rỗng được yêu cầu cho Firebase Realtime Database
    }

    public User(String birthday, String gender, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }
}

