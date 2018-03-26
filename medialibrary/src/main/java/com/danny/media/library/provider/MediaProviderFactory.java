package com.danny.media.library.provider;

import android.content.Context;

import com.danny.media.library.music.provider.MediaMusicProvider;
import com.danny.media.library.music.provider.MusicProvider;
import com.danny.media.library.video.provider.MediaVideoProvider;
import com.danny.media.library.video.provider.VideoProvider;

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
