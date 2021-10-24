package com.example.work;

public class User {
    private int id;
    private String username;
    private String password;
    private int gender;
    private String city;
    private String birthday;
    private int avatar;

    public User(int id, String username, String password, int gender, String city, String birthday, int avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.city = city;
        this.birthday = birthday;
        this.avatar = avatar;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
