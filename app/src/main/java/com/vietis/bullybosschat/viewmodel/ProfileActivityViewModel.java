package com.vietis.bullybosschat.viewmodel;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vietis.bullybosschat.BR;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.model.User;

public class ProfileActivityViewModel extends BaseObservable {

    private User user;
    private String userName;
    private String urlImg;
    private String urlBackground;
    private String numOfFriends;
    private String numOfFollows;
    private boolean isEditingProfile;
    private boolean isFriends;
    private String addFriends;
    private String follows;
    private int visiable;

    public ProfileActivityViewModel(boolean isEditingProfile, boolean isFriends) {
        setEditingProfile(isEditingProfile, isFriends);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR._all);
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setEditingProfile(boolean editingProfile) {
        isEditingProfile = editingProfile;
    }

    @Bindable
    public boolean isFriends() {
        return isFriends;
    }

    public void setFriends(boolean friends) {
        isFriends = friends;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getAddFriends() {
        return addFriends;
    }

    public void setAddFriends(String addFriends) {
        this.addFriends = addFriends;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public int getVisiable() {
        return visiable;
    }

    public void setVisiable(int visiable) {
        this.visiable = visiable;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getNumOfFriends() {
        return numOfFriends;
    }

    public void setNumOfFriends(String numOfFriends) {
        this.numOfFriends = numOfFriends;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getNumOfFollows() {
        System.out.println("Debug in getNumOfFollows :" + numOfFollows);
        return numOfFollows;
    }

    public void setNumOfFollows(String numOfFollows) {
        this.numOfFollows = numOfFollows;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUrlBackground() {
        if (urlBackground!=null){
            System.out.println("Debug: in getUrlImg: " + urlBackground);
            return urlBackground;
        }
        return "https://images.unsplash.com/photo-1519681393784-d120267933ba?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1000&q=80";
    }

    public void setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUrlImg() {
        System.out.println("Debug: in getUrlImg: " + urlImg);
        if (urlImg!=null){
            System.out.println("Debug: in getUrlImg: " + urlImg);
            return urlImg;
        }
        return "https://androidwave.com/wp-content/uploads/2019/03/loading-images-using-data-binding.jpeg";
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
        notifyPropertyChanged(BR._all);
    }

    public boolean isEditingProfile() {
        return isEditingProfile;
    }

    public void setEditingProfile(boolean editingProfile, boolean friends) {
        isEditingProfile = editingProfile;
        isFriends = friends;
        if (editingProfile){
            setAddFriends("");
            setFollows("");
            setVisiable(8);
        } else {
            if (friends){
                setAddFriends("friend");
                setFollows("followed");
                setVisiable(8);
            } else {
                setAddFriends("Add Friends");
                setFollows("Follows");
                setVisiable(0);
            }
        }
        notifyPropertyChanged(BR._all);
    }

    public void getProfile(final String userID) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        mData.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getId().equals(userID)) {
                    setUser(user);
                    setUserName(user.getUsername());
                    setUrlImg(user.getImageurl());
                    setUrlBackground(user.getImagecover());
                    setNumOfFollows(String.valueOf(user.getFollows().size() - 1));
                    setNumOfFriends(String.valueOf(user.getFriends().size() - 1));
                    System.out.println("Debug: in getProfile " + getUrlImg());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @BindingAdapter("bind:urlBackground")
    public static void loadBackground(ImageView view, String urlBackground) {
        System.out.println("Debug: imageUrl = " + urlBackground);
        if (urlBackground.equals("default")) {
            Glide.with(view.getContext())
                    .load("https://previews.123rf.com/images/lyubovtolstova/lyubovtolstova1801/lyubovtolstova180100155/93637593-watercolor-hand-drawn-abstract-horizontal-background-with-strains-blue-purple-gradient-watercolor-fi.jpg")
                    .into(view);
        } else {
            Glide.with(view.getContext())
                    .load(urlBackground)
                    .into(view);
        }
    }
}
