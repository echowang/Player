package com.danny.media.library.provider.filter;

import com.danny.media.library.model.Song;

/**
 * Created by tingw on 2018/1/22.
 * 根据歌曲路径进行过滤
 */

public class MusicPathFilter extends Filter<Song> {
    private String filter;

    public MusicPathFilter(String filter){
        this.filter = filter;
    }

    @Override
    public boolean performFiltering(Song song) {
        if (song == null){
            return false;
        }
        return song.getPath().equalsIgnoreCase(filter);
    }

    @Override
    public String getFilterName() {
        return "MusicPathFilter";
    }
}
