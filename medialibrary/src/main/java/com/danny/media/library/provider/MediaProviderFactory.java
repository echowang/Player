package com.danny.media.library.provider;

import android.content.Context;

import com.danny.media.library.provider.music.MediaMusicProvider;
import com.danny.media.library.provider.music.MusicProvider;
import com.danny.media.library.provider.video.VideoProvider;

/**
 * Created by tingw on 2018/1/2.
 */

public class MediaProviderFactory {
    private static final MediaProviderFactory ourInstance = new MediaProviderFactory();

    private MusicProvider musicProvider;

    public static MediaProviderFactory getInstance() {
        return ourInstance;
    }

    private MediaProviderFactory() {
    }

    public synchronized MusicProvider getMusciProvideo(Context context){
        synchronized (ourInstance){
            if (musicProvider == null){
                musicProvider = new MediaMusicProvider(context);
            }
        }
        return musicProvider;
    }

    public VideoProvider getVideoProvider(){
        return  new VideoProvider() {
            @Override
            public void scanMediaResources() {

            }
        };
    }
}
