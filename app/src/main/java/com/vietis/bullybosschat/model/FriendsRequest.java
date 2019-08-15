package com.vietis.bullybosschat.model;

public class FriendsRequest {

    private String sender;
    private String target;

    public FriendsRequest(String sender, String target) {
        this.sender = sender;
        this.target = target;
    }

    public FriendsRequest() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
