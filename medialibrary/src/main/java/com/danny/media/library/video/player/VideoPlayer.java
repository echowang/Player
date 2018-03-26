package com.danny.media.library.video.player;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.danny.media.library.video.model.Video;
import com.danny.media.library.player.Player;
import com.danny.media.library.player.PlayerScheduleListener;
import com.danny.media.library.player.PlayerStaue;
import com.danny.media.library.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by tingw on 2018/1/22.
 */

public class VideoPlayer implements Player<Video>, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {
    private final static String TAG = VideoPlayer.class.getSimpleName();
    private MediaPlayer mPlayer;
    private PlayerStaue playerStaue = PlayerStaue.STATE_IDLE;    //播放器的状态
    private Video playVideo;

    private PlayerScheduleListener<Video> scheduleListener;

    private VideoSurfaceViewCreater surfaceViewCreater;

    private Disposable disposable;

    public VideoPlayer(PlayerScheduleListener<Video> scheduleListener){
        this.scheduleListener = scheduleListener;
        this.mPlayer = new MediaPlayer();
        this.mPlayer.setOnBufferingUpdateListener(this);
        this.mPlayer.setOnPreparedListener(this);
        this.mPlayer.setOnCompletionListener(this);
        this.mPlayer.setOnErrorListener(this);
        this.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void play(Video video) {
        if (surfaceViewCreater != null){
            playVideo = video;
            playerStaue = PlayerStaue.STATE_PREPARING;

            try {
                mPlayer.reset();
                LogUtil.i(TAG,"video is " + video.getPath());
                mPlayer.setDataSource(video.getPath());
                mPlayer.setDisplay(surfaceViewCreater.onCreaterSurfaceHolder());
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            LogUtil.e(TAG,"surfaceViewCreater is null");
        }
    }

    @Override
    public void pause() {
        if (isPlaying()){
            mPlayer.pause();
        }
        playerStaue = PlayerStaue.STATE_PAUSE;
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void resume() {
        if (playerStaue == PlayerStaue.STATE_PAUSE){
            startPlaying();
        }
    }

    @Override
    public void stop() {
        if (isPlaying() || isPausing()){
            mPlayer.stop();
            mPlayer.reset();
        }

        playerStaue = PlayerStaue.STATE_IDLE;
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    @Override
    public int getPlayProgress() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return playerStaue == PlayerStaue.STATE_PLAYING;
    }

    @Override
    public boolean isPausing() {
        return playerStaue == PlayerStaue.STATE_PAUSE;
    }

    @Override
    public boolean isPreparing() {
        return playerStaue == PlayerStaue.STATE_PREPARING;
    }

    @Override
    public boolean isIdle() {
        return playerStaue == PlayerStaue.STATE_IDLE;
    }

    @Override
    public Video getPlaySource() {
        return playVideo;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        if (scheduleListener != null){
            scheduleListener.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        LogUtil.i("VideoPlayer onCompletion");
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        if (scheduleListener != null){
            scheduleListener.OnCompletion();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (isPreparing()){
            startPlaying();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    public void setSurfaceViewCreater(VideoSurfaceViewCreater surfaceViewCreater){
        this.surfaceViewCreater = surfaceViewCreater;
    }

    private void startPlaying(){
        playerStaue = PlayerStaue.STATE_PLAYING;
        mPlayer.start();
        if (scheduleListener != null){
            scheduleListener.OnChangeSource(playVideo);
        }

        Observable.interval(1000,1000,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (scheduleListener != null){
                            int currentPosition = mPlayer.getCurrentPosition();
                            scheduleListener.onPublish(currentPosition);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }
}
