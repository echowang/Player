package com.danny.media.library.file;

import android.content.Context;

/**
 * Created by tingw on 2018/1/2.
 */

class MediaProviderFactory {
    private static final MediaProviderFactory ourInstance = new MediaProviderFactory();

    static MediaProviderFactory getInstance() {
        return ourInstance;
    }

    private MediaProviderFactory() {
    }

    public MusicProvider getMusciProvideo(Context context){
        return new MediaMusicProvider(context);
    }

    public VideoProvider getVideoProvider(){
        return  new VideoProvider() {
            @Override
            public void scanMediaResources() {

            }
        };
    }
}
