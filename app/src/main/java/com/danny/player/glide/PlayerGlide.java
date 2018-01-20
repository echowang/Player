package com.danny.player.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by dannywang on 2018/1/20.
 * 负责调用Glide加载图片
 */

public class PlayerGlide {

    /**
     * 加载图片
     * @param context
     * @param image
     * @param imageView
     */
    public static void loadImage(Context context, int image, ImageView imageView){
        if (context == null || image == 0 || imageView == null){
            return;
        }

        GlideApp.with(context)
                .load(image)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 加载本地图片 圆角显示
     * @param context
     * @param placeholder
     * @param error
     * @param round
     * @param imagePath
     * @param imageView
     */
    public static void loadLocalRoundImage(Context context, int placeholder, int error, String imagePath, int round, ImageView imageView){
        if (context == null || placeholder == 0 || error == 0 || TextUtils.isEmpty(imagePath) || imageView == null){
            return;
        }

        GlideApp.with(context)
                .load(new File(imagePath))
                .centerCrop()
                .transform(new GlideRoundTransform(context,round))
                .placeholder(placeholder)
                .error(error)
                .fallback(error)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 加载本地图片 圆形显示
     * @param context
     * @param placeholder
     * @param error
     * @param imagePath
     * @param imageView
     */
    public static void loadLocalCircleImage(Context context, int placeholder, int error, String imagePath, ImageView imageView){
        if (context == null || placeholder == 0 || error == 0 || TextUtils.isEmpty(imagePath) || imageView == null){
            return;
        }

        GlideApp.with(context)
                .load(new File(imagePath))
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                .placeholder(placeholder)
                .error(error)
                .fallback(error)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 将国图片模糊化
     * @param context
     * @param placeholder
     * @param error
     * @param imagePath
     * @param radiu
     * @param sampling
     * @param imageView
     */
    public static void loadLocalBlurImage(Context context, int placeholder, int error, String imagePath, int radiu,int sampling, ImageView imageView){
        if (context == null || placeholder == 0 || error == 0 || TextUtils.isEmpty(imagePath) || imageView == null){
            return;
        }

        GlideApp.with(context)
                .load(new File(imagePath))
                .centerCrop()
                .transform(new GlideBlurTransformation(context,radiu,sampling))
                .placeholder(placeholder)
                .error(error)
                .fallback(error)
                .dontAnimate()
                .into(imageView);
    }
}
