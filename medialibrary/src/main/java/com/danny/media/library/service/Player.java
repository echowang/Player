package com.danny.media.library.service;

import com.danny.media.library.model.Song;

/**
 * Created by tingw on 2018/1/4.
 */

public interface Player {
    void play(Song song);
    void pause();
    void resume();
    void stop();
    void seekTo(int msec);
    int getPlayProgress();
    boolean isPlaying();
    boolean isPausing();
    boolean isPreparing();
    boolean isIdle();
}


