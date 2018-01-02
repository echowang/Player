package com.danny.media.library.file;

import android.content.Context;

import com.danny.media.library.model.Song;
import com.danny.media.library.utils.ChineseToPinyin;
import com.danny.media.library.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dannywang on 2017/12/29.
 */

public abstract class MusicProvider implements MediaProvider {
    protected Context context;
    protected List<Song> songList = new ArrayList<>();
    protected Map<String,List<Song>> singerList = new HashMap<>();
    protected Map<String,List<Song>> albumList = new HashMap<>();
    protected Map<String,List<Song>> folderList = new HashMap<>();
    protected MusicSourceChangeListener musicSourceChangeListener;

    public MusicProvider(Context context){
        this.context = context;
    }

    @Override
    public abstract void scanMediaResources();

    public void setMusicSourceChangeListener(MusicSourceChangeListener musicSourceChangeListener){
        this.musicSourceChangeListener = musicSourceChangeListener;
    }

    /**
     * 获取所有歌曲
     * @return
     */
    public List<Song> getMusicList() {
        return songList;
    }

    /**
     * 根据歌手分类查询歌曲
     * @return
     */
    public Map<String,List<Song>> getMusicListBySinger(){
        return singerList;
    }

    /**
     * 根据专辑分类查询歌曲
     * @return
     */
    public Map<String,List<Song>> getMusicListByAlbum(){
        return albumList;
    }

    /**
     * 根据文件夹查询歌曲
     * @return
     */
    public Map<String,List<Song>> getMusicListByFolder(){
        return folderList;
    }

    public List<Song> getMusicList(int startPosition,int endPosition){
        return songList.subList(startPosition,endPosition);
    }

    public Song getMusic(int position){
        if (songList != null && position >= 0 && position < songList.size()){
            return songList.get(position);
        }

        return null;
    }

    /**
     * 将歌曲进行排序
     */
    protected void sortMusicList(){
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                if (lhs.getTitle().length() > 0 && rhs.getTitle().length() > 0) {
                    //如果两者都是汉字开头,则获取每个汉字的首字母
                    if (Util.isChinese(lhs.getTitle().charAt(0)) && Util.isChinese(rhs.getTitle().charAt(0))) {
                        StringBuilder lhsPY = new StringBuilder();
                        StringBuilder rhsPY = new StringBuilder();
                        for (int i = 0; i < lhs.getTitle().length(); i++) {
                            lhsPY.append(ChineseToPinyin.getFirstPinYin(lhs.getTitle().charAt(i) + ""));
                        }
                        for (int i = 0; i < rhs.getTitle().length(); i++) {
                            rhsPY.append(ChineseToPinyin.getFirstPinYin(rhs.getTitle().charAt(i) + ""));
                        }
                        return lhsPY.toString().compareToIgnoreCase(rhsPY.toString());
                    } else {
                        return lhs.getTitle().compareToIgnoreCase(rhs.getTitle());
                    }
                }
                return 0;
            }
        });

        categoryMusicList();
    }

    /**
     * 将歌曲进行分类处理
     */
    private void categoryMusicList(){
        for (Song song: songList) {

        }
    }

    public interface MusicSourceChangeListener{
        void onStartScan();
        void onMusicSourceChange(int startPosition,int endPosition);
        void onScanFinish();
    }
}
