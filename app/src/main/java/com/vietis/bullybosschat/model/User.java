package com.vietis.bullybosschat.model;

import java.util.ArrayList;

public class User {
    private String id;
    private String imageurl;
    private String state;
    private String username;
    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    private ArrayList<String> friends;

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

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

    public User(String id, String imageurl, String state, String username, String search, ArrayList<String> friends) {
        this.id = id;
        this.imageurl = imageurl;
        this.state = state;
        this.username = username;
        this.search = search;
        this.friends = friends;
    }

    public User() {
    }
}
