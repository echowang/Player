package com.danny.media.library.provider;

/**
 * Created by tingw on 2018/1/23.
 */

public abstract class MediaProvider {
    protected MediaProviderListener mediaProviderListener;

    protected abstract void queryMediaResources();

    public void setMediaProviderListener(MediaProviderListener mediaProviderListener){
        this.mediaProviderListener = mediaProviderListener;
    }

    public MediaProviderListener getMediaProviderListener(){
        return mediaProviderListener;
    }

    public abstract void loadMediaResources();
}
