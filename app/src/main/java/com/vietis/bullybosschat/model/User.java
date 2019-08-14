package com.vietis.bullybosschat.model;

import java.util.ArrayList;

public class User {
    private String id;
    private String imageurl;
    private String state;
    private String username;
    private String search;
    private ArrayList<String> friends;
    private ArrayList<String> follows;
    private  String imagecover;


    public ArrayList<String> getFollows() {
        return follows;
    }
    public void setFollows(ArrayList<String> follows) {
        this.follows = follows;
    }

    public void setImagecover(String imagecover) {
        this.imagecover = imagecover;
    }

    public String getImagecover() {
        return imagecover;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

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


    public User(String id, String imageurl, String state, String username, String search,String imagecover, ArrayList<String> friends, ArrayList<String> follows) {
        this.id = id;
        this.imageurl = imageurl;
        this.state = state;
        this.username = username;
        this.search = search;
        this.friends = friends;
        this.follows = follows;
        this.imagecover =  imagecover;
    }

    public User() {
    }
}
