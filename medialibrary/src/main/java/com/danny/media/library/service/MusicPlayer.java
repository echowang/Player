package com.danny.media.library.service;

import android.media.MediaPlayer;
import android.os.Handler;

import com.danny.media.library.model.Song;

import java.io.IOException;

/**
 * Created by tingw on 2018/1/4.
 */

public class MusicPlayer implements Player, MediaPlayer.OnCompletionListener,MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnPreparedListener{
    private MediaPlayer mPlayer;
    private PlayerStaue playerStaue = PlayerStaue.STATE_IDLE;    //播放器的状态
    private Song playSong;   //当前正在播放的歌曲

    private PlayerScheduleListener<Song> scheduleListener;

    private Handler mHandler;
    private static final long TIME_UPDATE = 500L;

    private Runnable mPublishRunnable = new Runnable(){
        @Override
        public void run() {
            if (isPlaying() && scheduleListener != null){
                scheduleListener.onPublish(mPlayer.getCurrentPosition());
            }

            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    public MusicPlayer(PlayerScheduleListener scheduleListener){
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        this.scheduleListener = scheduleListener;
        this.mHandler = new Handler();
    }

    @Override
    public void play(Song song) {
        try {
            playSong = song;
            mPlayer.reset();
            mPlayer.setDataSource(song.getPath());
            mPlayer.prepareAsync();
            playerStaue = PlayerStaue.STATE_PREPARING;
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mPlayer.pause();
        playerStaue = PlayerStaue.STATE_PAUSE;
        mHandler.removeCallbacks(mPublishRunnable);
    }

    @Override
    public void resume() {
        if (playerStaue == PlayerStaue.STATE_PAUSE){
            startPlaying();
        }
    }

    @Override
    public void stop() {
        pause();
        playSong = null;
        mPlayer.reset();
        playerStaue = PlayerStaue.STATE_IDLE;
        mHandler.removeCallbacks(mPublishRunnable);
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
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (scheduleListener != null){
            scheduleListener.OnCompletion();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (scheduleListener != null){
            scheduleListener.onBufferingUpdate(i);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (isPreparing()){
            startPlaying();
        }
    }

    private void startPlaying(){
        playerStaue = PlayerStaue.STATE_PLAYING;
        mPlayer.start();
        mHandler.post(mPublishRunnable);
        if (scheduleListener != null){
            scheduleListener.OnChangeSource(playSong);
        }
    }

    public Song getPlaySong(){
        return playSong;
    }
}
