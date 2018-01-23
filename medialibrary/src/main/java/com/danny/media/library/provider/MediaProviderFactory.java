package com.danny.media.library.provider;

import android.content.Context;

import com.danny.media.library.provider.music.MediaMusicProvider;
import com.danny.media.library.provider.music.MusicProvider;
import com.danny.media.library.provider.video.MediaVideoProvider;
import com.danny.media.library.provider.video.VideoProvider;

/**
 * Created by tingw on 2018/1/2.
 */

public class MediaProviderFactory {
    private static final MediaProviderFactory ourInstance = new MediaProviderFactory();

    private MusicProvider musicProvider;
    private VideoProvider videoProvider;

    public static MediaProviderFactory getInstance() {
        return ourInstance;
    }

    private MediaProviderFactory() {
    }

    public synchronized MusicProvider getMusciProvideo(Context context){
        synchronized (ourInstance){
            if (musicProvider == null && context != null){
                musicProvider = new MediaMusicProvider(context.getApplicationContext());
            }
        }
        return musicProvider;
    }

    public VideoProvider getVideoProvider(Context context){
        if (videoProvider == null && context != null){
            videoProvider = new MediaVideoProvider(context.getApplicationContext());
        }
        return  videoProvider;
    }
}
