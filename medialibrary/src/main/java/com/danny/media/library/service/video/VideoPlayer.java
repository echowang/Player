package com.danny.media.library.service.video;

import android.media.MediaPlayer;

import com.danny.media.library.model.Video;
import com.danny.media.library.service.Player;
import com.danny.media.library.service.PlayerScheduleListener;
import com.danny.media.library.service.PlayerStaue;

/**
 * Created by tingw on 2018/1/22.
 */

public class VideoPlayer implements Player<Video>, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {
    private MediaPlayer mPlayer;
    private PlayerStaue playerStaue = PlayerStaue.STATE_IDLE;    //播放器的状态
    private Video playVideo;

    private PlayerScheduleListener<Video> scheduleListener;

    public VideoPlayer(PlayerScheduleListener<Video> scheduleListener){
        this.scheduleListener = scheduleListener;
        this.mPlayer = new MediaPlayer();
        this.mPlayer.setOnBufferingUpdateListener(this);
        this.mPlayer.setOnPreparedListener(this);
        this.mPlayer.setOnCompletionListener(this);
    }

    @Override
    public void play(Video video) {
        playVideo = video;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

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
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }
}
