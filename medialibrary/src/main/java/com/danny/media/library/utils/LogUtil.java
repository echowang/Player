package com.danny.media.library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.danny.media.library.BuildConfig;

/**
 * Created by tingw on 2018/1/19.
 */

public class LogUtil {
    private final static String TAG = LogUtil.class.getSimpleName();

    public static void v(String msg){
        v(TAG,msg);
    }

    public static void v(String tag, String msg){
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }

        if (BuildConfig.DEBUG){
            Log.v(tag,msg);
        }
    }

    public static void d(String msg){
        d(TAG,msg);
    }

    public static void d(String tag, String msg){
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }

        if (BuildConfig.DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String msg){
        i(TAG,msg);
    }

    public static void i(String tag, String msg){
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }

        if (BuildConfig.DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void w(String msg){
        w(TAG,msg);
    }

    public static void w(String tag, String msg){
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }

        if (BuildConfig.DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void e(String msg){
        e(TAG,msg);
    }

    public static void e(String tag, String msg){
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(msg)){
            return;
        }

        if (BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

}
