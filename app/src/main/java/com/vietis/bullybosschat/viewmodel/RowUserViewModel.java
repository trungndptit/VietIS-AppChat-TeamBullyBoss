package com.vietis.bullybosschat.viewmodel;

import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.vietis.bullybosschat.BR;
import com.vietis.bullybosschat.R;
import com.vietis.bullybosschat.model.User;

public class RowUserViewModel extends BaseObservable {
    private User user;
    private String username;
    private String urlImg;

    public RowUserViewModel(User user) {
        this.username = user.getUsername();
        this.urlImg = user.getImageurl();
        setUser(user);
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @BindingAdapter("bind:urlImg")
    public static void loadImage(ImageView view, String urlImg) {
        System.out.println("Debug: imageUrl = " + urlImg);
        if (urlImg.equals("default")) {
            view.setImageResource(R.drawable.ic_avatar);
        } else {
            Glide.with(view.getContext())
                    .load(urlImg)
                    .circleCrop()
                    .into(view);
        }
    }
}
