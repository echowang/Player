package com.danny.media.library.file;

import com.danny.media.library.model.Video;

import java.util.List;

/**
 * Created by dannywang on 2017/12/29.
 */

public abstract class VideoProvider{
    private List<Video> videoList;

    public abstract void scanMediaResources();

    public List<Video> getMediaList() {
        return videoList;
    }
}
