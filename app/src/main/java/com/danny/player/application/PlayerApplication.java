package com.danny.player.application;

import android.app.Application;
import android.content.Intent;

import com.danny.media.library.model.Song;
import com.danny.media.library.service.MusicPlayerService;
import com.danny.media.library.service.PlayerService;

/**
 * Created by tingw on 2018/1/4.
 */

public class PlayerApplication extends Application {
    private static PlayerApplication application;

    private PlayerService<Song> musicPlayerService;

    public static PlayerApplication getApplication(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public void setMusicPlayerService(PlayerService<Song> playerService){
        this.musicPlayerService = playerService;
    }

    public PlayerService<Song> getMusicPlayerService(){
        return musicPlayerService;
    }
}
