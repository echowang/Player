package com.danny.media.library.player;

/**
 * Created by tingw on 2018/1/4.
 */

public interface Player<T> {
    void play(T t);
    void pause();
    void resume();
    void stop();
    void seekTo(int msec);
    int getPlayProgress();
    boolean isPlaying();
    boolean isPausing();
    boolean isPreparing();
    boolean isIdle();
    T getPlaySource();
}


