package com.vietis.bullybosschat.view.adapter.bindingadapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.vietis.bullybosschat.R;

@SuppressWarnings("unused")
public class ImageBindingAdapter {
    @BindingAdapter("urlImg")
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
