package com.danny.media.library.player;

import java.util.List;

/**
 * Created by tingw on 2018/1/23.
 */

public interface IServiceUIRefreshListener<T>{
    void onRefreshSourceList(List<T> songList);
    void onPublish(T t,int progress);
    void onBufferingUpdate(T t,int percent);
    void onSourceChange(T t);
}
