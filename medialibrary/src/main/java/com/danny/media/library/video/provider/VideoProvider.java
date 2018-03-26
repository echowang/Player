package com.danny.media.library.video.provider;

import android.content.Context;

import com.danny.media.library.video.model.Video;
import com.danny.media.library.provider.MediaProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dannywang on 2017/12/29.
 */

public abstract class VideoProvider extends MediaProvider{
    protected Context context;
    protected List<Video> videoList;

    public VideoProvider(Context context){
        this.context = context;
        this.videoList = new ArrayList<>();
    }

    public List<Video> getMediaList() {
        return videoList;
    }

    @Override
    public void loadMediaResources() {
        queryMediaResources();

        if (mediaProviderListener != null){
            mediaProviderListener.onScanFinish();
        }
    }
}
