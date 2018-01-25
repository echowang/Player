package com.danny.player.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.danny.media.library.model.Song;
import com.danny.media.library.service.PlayerService;
import com.danny.media.library.service.music.MusicPlayerService;
import com.danny.media.library.service.video.VideoPlayerService;

import java.util.ArrayList;

/**
 * Created by tingw on 2018/1/4.
 */

public class PlayerApplication extends Application {
    private static PlayerApplication application;

    private PlayerService<Song> musicPlayerService;
    private VideoPlayerService videoPlayerService;

    public static PlayerApplication getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void setMusicPlayerService(PlayerService<Song> playerService){
        this.musicPlayerService = playerService;
    }

    public PlayerService<Song> getMusicPlayerService(){
        return musicPlayerService;
    }

    public void startMusicPlayerService(){
        if (!serviceIsRunning(MusicPlayerService.class)){
            Intent intent = new Intent(this, MusicPlayerService.class);
            startService(intent);
        }
    }

    public void stopMusicPlayerService(){
        if (musicPlayerService != null){
            musicPlayerService.stopPlayerService();
            musicPlayerService = null;
        }
    }

    public void setVideoPlayerService(VideoPlayerService videoPlayerService){
        this.videoPlayerService = videoPlayerService;
    }

    public VideoPlayerService getVideoPlayerService(){
        return videoPlayerService;
    }

    public void startVideoPlayerService(){
        if (!serviceIsRunning(VideoPlayerService.class)){
            Intent intent = new Intent(this, VideoPlayerService.class);
            startService(intent);
        }
    }

    public void stopVideoPlayerService(){
        if (videoPlayerService != null){
            videoPlayerService.stopPlayerService();
            videoPlayerService = null;
        }
    }

    /**
     * 判断Service是否已经启动
     * @param clazz
     * @return
     */
    private boolean serviceIsRunning(Class clazz) {
        if (clazz == null){
            return false;
        }
        String className = clazz.getName();
        ActivityManager myManager = (ActivityManager) this
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }
}
