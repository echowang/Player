package com.danny.media.library.file;

import android.content.Context;

import com.danny.media.library.model.Song;
import com.danny.media.library.utils.ChineseToPinyin;
import com.danny.media.library.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dannywang on 2017/12/29.
 */

public abstract class MusicProvider{
    protected Context context;
    protected List<Song> songList = new ArrayList<>();
    protected Map<String,List<Song>> singerList = new HashMap<>();
    protected Map<String,List<Song>> albumList = new HashMap<>();
    protected Map<String,List<Song>> folderList = new HashMap<>();
    protected IMusicScanListener IMusicScanListener;

    public MusicProvider(Context context){
        this.context = context;
    }

    protected abstract void scanMediaResources();

    private void clear(){
        songList.clear();
        singerList.clear();
        albumList.clear();
        folderList.clear();
    }

    /**
     * 对外接口，启动加载本地音乐
     */
    public void loadMusic(){
        if (IMusicScanListener != null){
            IMusicScanListener.onStartScan();
        }
        clear();
        scanMediaResources();
        sortList();
        categoryMusicList();
        if (IMusicScanListener != null){
            IMusicScanListener.onScanFinish();
        }
    }

    public void setIMusicScanListener(IMusicScanListener IMusicScanListener){
        this.IMusicScanListener = IMusicScanListener;
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

//    public List<Song> getMusicList(int startPosition,int endPosition){
//        return songList.subList(startPosition,endPosition);
//    }

    /**
     * 将歌曲进行排序
     */
    protected void sortList(){
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
    }

    /**
     * 将歌曲进行分类处理
     */
    private void categoryMusicList(){
        for (Song song: songList) {
            //按歌手分类
            String singer = song.getArtist();
            if (singerList.containsKey(singer)){
                List<Song> temp = singerList.get(singer);
                temp.add(song);
            }else{
                List<Song> temp = new ArrayList<>();
                temp.add(song);
                singerList.put(singer,temp);
            }

            //按专辑分类
            String albumId = String.valueOf(song.getAlbumId());
            if (albumList.containsKey(albumId)){
                List<Song> temp = albumList.get(albumId);
                temp.add(song);
            }else{
                List<Song> temp = new ArrayList<>();
                temp.add(song);
                albumList.put(albumId,temp);
            }

            //按文件夹分类
            String filePath = song.getPath();
            File tempFile = new File(filePath);
            String folder = tempFile.getParent();
            if (folderList.containsKey(folder)){
                List<Song> temp = folderList.get(folder);
                temp.add(song);
            }else{
                List<Song> temp = new ArrayList<>();
                temp.add(song);
                folderList.put(folder,temp);
            }
        }
    }

    public interface IMusicScanListener {
        void onStartScan();
//        void onMusicSourceChange(int startPosition,int endPosition);
        void onScanFinish();
    }
}
