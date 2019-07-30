package com.vietis.bullybosschat.model;

public class User {
    private String id;
    private String imageurl;
    private String state;
    private  String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String id, String imageurl, String state, String username) {
        this.id = id;
        this.imageurl = imageurl;
        this.state = state;
        this.username = username;
    }

    public User() {
    }
}
