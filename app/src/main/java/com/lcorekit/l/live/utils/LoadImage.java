package com.lcorekit.l.live.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.lcorekit.l.douyu.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Andy on 2016/11/8.
 */

public class LoadImage {
    //加载展示圆形图片
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        Uri parseUrl = Uri.parse(url);
        Picasso.with(context)
                .load(parseUrl)
                .placeholder(R.drawable.loading)//指定图片未加载成功前显示的图片
                .error(R.drawable.not_showed)//指定图片加载失败时显示的图片
                .transform(new CircleTransform())//指定图片的形状为圆形
                .into(imageView);//指定图片展示的控件
    }

    //加载展示普通图片
    public static void loadNormalImage(Context context, String url, ImageView imageView) {
        Uri parseUrl = Uri.parse(url);
        Picasso.with(context)
                .load(parseUrl)
                .placeholder(R.drawable.image_loading)//指定图片未加载成功前显示的图片
                .error(R.drawable.not_showed)//指定图片加载失败时显示的图片
                .into(imageView);//指定图片展示的控件
    }
}
