package com.danny.media.library.provider.filter;

import com.danny.media.library.model.Song;

/**
 * Created by tingw on 2018/1/22.
 *  对歌曲的文件长度和播放时长过滤
 */

public class MusicSizeFilter extends Filter<Song> {
    public final static long FILE_SIZE = 1024 * 1024;   //默认过滤的文件大小 1m
    public final static int DURATION = 30;    //默认过滤的播放时长 30s

    private long size;
    private int duration;

    public MusicSizeFilter(){
        this(FILE_SIZE,DURATION);
    }

    public MusicSizeFilter(long size,int duration){
        this.size = size;
        this.duration = duration;
    }

    @Override
    public boolean performFiltering(Song song) {
        if (song == null){
            return false;
        }

//        根据时间和大小，来判断所筛选的media 是否为音乐文件，具体规则为筛选小于30秒和1m一下的
        int time = song.getDuration();
        time /= 1000;
        int minute = time / 60;
//      int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        if(minute <= 0 && second <= duration) {
            return  false;
        }
        if(song.getFileSize() <= size){
            return false;
        }
        return true;
    }

    @Override
    public String getFilterName() {
        return "MusicSizeFilter";
    }
}
