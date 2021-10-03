package com.zy.deskpic.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zy.deskpic.ZyApplication;

public class ImageHelper {
    public static void showPic(ImageView imageView, Uri uri) {
        try {
            Glide.with(ZyApplication.getInstance()).load(uri).into(imageView);
        } catch (Exception e) {
        }
    }
}
