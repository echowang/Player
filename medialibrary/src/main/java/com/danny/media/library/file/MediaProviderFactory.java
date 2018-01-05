package com.danny.media.library.file;

import android.content.Context;

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
