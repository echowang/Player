package com.danny.player.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.danny.media.library.utils.ImageUtils;
import com.danny.player.R;

/**
 * Created by tingw on 2018/1/18.
 */

public class MusicAlbumCoverView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final long TIME_UPDATE = 50L;
    private static final float DISC_ROTATION_INCREASE = 0.5f;

    private static final float NEEDLE_ROTATION_PLAY = 0.0f;
    private static final float NEEDLE_ROTATION_PAUSE = -25.0f;

    private Drawable mTopLine;
    private Drawable mCoverBorder;
    private int mTopLineHeight;
    private int mCoverBorderWidth;
    private Bitmap mDiscBitmap;
    private Bitmap mCoverBitmap;
    private Bitmap mNeedleBitmap;

    private ValueAnimator mPlayAnimator;
    private ValueAnimator mPauseAnimator;

    private Matrix mDiscMatrix = new Matrix();
    private Matrix mCoverMatrix = new Matrix();
    private Matrix mNeedleMatrix = new Matrix();

    // 图片起始坐标
    private Point mDiscPoint = new Point();
    private Point mCoverPoint = new Point();
    private Point mNeedlePoint = new Point();
    // 旋转中心坐标
    private Point mDiscCenterPoint = new Point();
    private Point mCoverCenterPoint = new Point();
    private Point mNeedleCenterPoint = new Point();

    private float mDiscRotation = 0.0f;
    private float mNeedleRotation = NEEDLE_ROTATION_PAUSE;

    private Handler mHandler = new Handler();
    private boolean isPlaying = false;

    private Runnable mRotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                mDiscRotation += DISC_ROTATION_INCREASE;
                if (mDiscRotation >= 360) {
                    mDiscRotation = 0;
                }
                invalidate();
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    public MusicAlbumCoverView(Context context) {
        super(context);
    }

    public MusicAlbumCoverView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MusicAlbumCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MusicAlbumCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView(){
        mTopLine = getResources().getDrawable(R.drawable.play_page_cover_top_line_shape);
        mCoverBorder = getResources().getDrawable(R.drawable.play_page_cover_border_shape);
        mTopLineHeight = dp2px(1);
        mCoverBorderWidth = dp2px(1);
        mDiscBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_disc);
        mDiscBitmap = ImageUtils.resizeImage(mDiscBitmap, (int) (getScreenWidth() * 0.75),
                (int) (getScreenWidth() * 0.75));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_default_cover);
        mCoverBitmap = ImageUtils.resizeImage(bitmap, getScreenWidth() / 2, getScreenWidth() / 2);
        mNeedleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_needle);
        mNeedleBitmap = ImageUtils.resizeImage(mNeedleBitmap, (int) (getScreenWidth() * 0.25),
                (int) (getScreenWidth() * 0.375));

        mPlayAnimator = ValueAnimator.ofFloat(NEEDLE_ROTATION_PAUSE, NEEDLE_ROTATION_PLAY);
        mPlayAnimator.setDuration(300);
        mPlayAnimator.addUpdateListener(this);
        mPauseAnimator = ValueAnimator.ofFloat(NEEDLE_ROTATION_PLAY, NEEDLE_ROTATION_PAUSE);
        mPauseAnimator.setDuration(300);
        mPauseAnimator.addUpdateListener(this);
    }

    private void initSize(){
        int discOffsetY = mNeedleBitmap.getHeight() / 2;
        mDiscPoint.x = (getWidth() - mDiscBitmap.getWidth()) / 2;
        mDiscPoint.y = discOffsetY;
        mCoverPoint.x = (getWidth() - mCoverBitmap.getWidth()) / 2;
        mCoverPoint.y = discOffsetY + (mDiscBitmap.getHeight() - mCoverBitmap.getHeight()) / 2;
        mNeedlePoint.x = getWidth() / 2 - mNeedleBitmap.getWidth() / 6;
        mNeedlePoint.y = -mNeedleBitmap.getWidth() / 6;
        mDiscCenterPoint.x = getWidth() / 2;
        mDiscCenterPoint.y = mDiscBitmap.getHeight() / 2 + discOffsetY;
        mCoverCenterPoint.x = mDiscCenterPoint.x;
        mCoverCenterPoint.y = mDiscCenterPoint.y;
        mNeedleCenterPoint.x = mDiscCenterPoint.x;
        mNeedleCenterPoint.y = 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1.绘制顶部虚线
        mTopLine.setBounds(0, 0, getWidth(), mTopLineHeight);
        mTopLine.draw(canvas);
        // 2.绘制黑胶唱片外侧半透明边框
        mCoverBorder.setBounds(mDiscPoint.x - mCoverBorderWidth, mDiscPoint.y - mCoverBorderWidth,
                mDiscPoint.x + mDiscBitmap.getWidth() + mCoverBorderWidth, mDiscPoint.y +
                        mDiscBitmap.getHeight() + mCoverBorderWidth);
        mCoverBorder.draw(canvas);
        // 3.绘制黑胶
        // 设置旋转中心和旋转角度，setRotate和preTranslate顺序很重要
        mDiscMatrix.setRotate(mDiscRotation, mDiscCenterPoint.x, mDiscCenterPoint.y);
        // 设置图片起始坐标
        mDiscMatrix.preTranslate(mDiscPoint.x, mDiscPoint.y);
        canvas.drawBitmap(mDiscBitmap, mDiscMatrix, null);
        // 4.绘制封面
        mCoverMatrix.setRotate(mDiscRotation, mCoverCenterPoint.x, mCoverCenterPoint.y);
        mCoverMatrix.preTranslate(mCoverPoint.x, mCoverPoint.y);
        canvas.drawBitmap(mCoverBitmap, mCoverMatrix, null);
        // 5.绘制指针
        mNeedleMatrix.setRotate(mNeedleRotation, mNeedleCenterPoint.x, mNeedleCenterPoint.y);
        mNeedleMatrix.preTranslate(mNeedlePoint.x, mNeedlePoint.y);
        canvas.drawBitmap(mNeedleBitmap, mNeedleMatrix, null);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mNeedleRotation = (float) animation.getAnimatedValue();
        invalidate();
    }

    public void initDefaultStatue(boolean isPlaying){
        mNeedleRotation = isPlaying ? NEEDLE_ROTATION_PLAY : NEEDLE_ROTATION_PAUSE;
        invalidate();
    }

    public void setCoverBitmap(Bitmap bitmap){
        if (bitmap == null){
            return;
        }
        bitmap = ImageUtils.resizeImage(bitmap, getScreenWidth() / 2, getScreenWidth() / 2);
        mCoverBitmap = ImageUtils.createCircleImage(bitmap);
        mDiscRotation = 0.0f;
        invalidate();
    }

    public void startAnimation(){
        if (isPlaying){
            return;
        }
        isPlaying = true;
        mPlayAnimator.start();
        mHandler.postDelayed(mRotationRunnable,TIME_UPDATE);
    }

    public void stopAnimation(){
        if (!isPlaying){
            return;
        }
        isPlaying = false;
        mHandler.removeCallbacks(mRotationRunnable);
        mPauseAnimator.start();
    }
}
