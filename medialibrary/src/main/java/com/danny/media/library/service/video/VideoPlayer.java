package com.danny.media.library.service.video;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.danny.media.library.model.Video;
import com.danny.media.library.service.Player;
import com.danny.media.library.service.PlayerScheduleListener;
import com.danny.media.library.service.PlayerStaue;
import com.danny.media.library.utils.LogUtil;

import java.io.IOException;

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
        playerStaue = PlayerStaue.STATE_PAUSE;
    }

    @Override
    public void resume() {
        if (playerStaue == PlayerStaue.STATE_PAUSE){
            startPlaying();
        }
    }

    @Override
    public void stop() {
        playerStaue = PlayerStaue.STATE_IDLE;
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
    }
}
