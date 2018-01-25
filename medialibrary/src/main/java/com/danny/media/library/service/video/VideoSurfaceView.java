package com.danny.media.library.service.video;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.danny.media.library.utils.LogUtil;

/**
 * Created by tingw on 2018/1/25.
 */

public class VideoSurfaceView extends SurfaceView {
    private final static String TAG = VideoSurfaceView.class.getSimpleName();

    public VideoSurfaceView(Context context) {
        this(context,null);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSurfaceView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSurfaceView();
    }

    private void initSurfaceView(){
        this.getHolder().setKeepScreenOn(true);
        this.getHolder().addCallback(new VideoSurfaceHolder());
    }

    public class VideoSurfaceHolder implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            LogUtil.i(TAG,"surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            LogUtil.i(TAG,"surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            LogUtil.i(TAG,"surfaceDestroyed");
        }
    }
}
