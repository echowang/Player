package com.danny.media.library.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by tingw on 2018/1/18.
 */

public abstract class PlayerService<T> extends Service {
    private PlayerBinder playerBinder;

    public abstract void setUIRefreshListener(IServiceUIRefreshListener uiRefreshListener);
    public abstract List<T> getPlaySourceList();
    public abstract T getPlaySource();
    public abstract void play(T t);
    public abstract void pause();
    public abstract void resume();
    public abstract void stop();
    public abstract void next();
    public abstract void prev();
    public abstract void seekTo(int progress);
    public abstract int getPlayProgress();
    public abstract boolean isPlaying();
    public abstract boolean isAutoPlay();
    public abstract void setPlayerModel(PlayerModel model);
    public abstract PlayerModel getPlayerModel();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (playerBinder == null){
            playerBinder = new PlayerBinder();
        }
        return playerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        playerBinder = null;
        return super.onUnbind(intent);
    }

    public class PlayerBinder extends Binder{
        public PlayerService getPlayerService(){
            return PlayerService.this;
        }
    }

    public interface IServiceUIRefreshListener<T>{
        void onRefreshMusicList(List<T> songList);
        void onPublish(T t,int progress);
        void onBufferingUpdate(T t,int percent);
        void onMusicChange(T t);
    }
}
