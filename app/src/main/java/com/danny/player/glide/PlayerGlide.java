package com.danny.player.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
     * @param radiu
     * @param sampling
     * @param imageView
     */
    public static void loadLocalBlurImage(Context context, int image, int radiu,int sampling, ImageView imageView){
        if (context == null || image == 0 || imageView == null){
            return;
        }

        GlideApp.with(context)
                .load(image)
                .centerCrop()
                .transform(new GlideBlurTransformation(context,radiu,sampling))
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int dstWidth, int dstHeight) {
        if (source == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
    }

    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }

        int length = Math.min(source.getWidth(), source.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
