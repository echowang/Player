package com.danny.media.library.service;

/**
 * Created by tingw on 2018/1/4.
 * 播放音乐的进度监听
 */

public interface PlayerScheduleListener<T> {
    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 播放结束
     */
    void OnCompletion();

    /**
     * 切换正在播放的源
     */
    void OnChangeSource(T t);
}
