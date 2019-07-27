package com.vietis.bullybosschat.chat.model;

public class User {
    private String mAvatar;
    private String mName;
    private String mTime;
    private  String mLastMess;

    public User(String mAvatar, String mName, String mTime, String mLastMess) {

        this.mAvatar = mAvatar;
        this.mName = mName;
        this.mTime = mTime;
        this.mLastMess = mLastMess;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmLastMess() {
        return mLastMess;
    }

    public void setmLastMess(String mLastMess) {
        this.mLastMess = mLastMess;
    }
}
