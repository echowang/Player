package com.danny.media.library.provider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.danny.media.library.model.Lrc;
import com.danny.media.library.model.Song;
import com.danny.media.library.utils.ChineseToPinyin;
import com.danny.media.library.utils.LogUtil;
import com.danny.media.library.utils.StringUtil;

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
    private final static String TAG = MusicProvider.class.getSimpleName();

    private final static String ALBUM_DIR = "album";
    private final static String ALBUM_SUFFIX = ".jpg";

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
     * 对外接口，启动查找本地音乐
     */
    public void loadMusic(){
        LogUtil.i(TAG,"loadMusic");
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

    /**
     * 根据歌曲查找歌词文件
     * @param song
     * @return
     */
    public Lrc findLrcBySong(Song song){
        if (song == null){
            return null;
        }
        LogUtil.i(TAG,"findLrcBySong");
        File songFile = new File(song.getPath());
        if (songFile.exists()){
            String parentPath = songFile.getParentFile().getParent();
            String name = song.getTitle() + "_" + song.getArtist();
            String lrcPath = parentPath + File.separator + Lrc.DIR + File.separator + name + Lrc.SUFFIX;
            LogUtil.i(TAG,"findLrcBySong parentPath : " + parentPath);
            LogUtil.i(TAG,"findLrcBySong name : " + name);
            LogUtil.i(TAG,"findLrcBySong lrcPath : " + lrcPath);
            File lrcFile = new File(lrcPath);
            if (lrcFile.exists()){
                Lrc lrc = new Lrc();
                lrc.setType(Lrc.LrcType.LOCAL);
                lrc.setLrcFile(lrcFile);
                return lrc;
            }
        }
        return null;
    }

    /**
     * 通过 歌曲 查询专辑图片
     * @param song
     * @return
     */
    public Bitmap getAlbumImage(Context context,Song song){
        if (context == null || song == null){
            return null;
        }
        long albumId = song.getAlbumId();
        Bitmap bitmap = getAlbumArt(context,String.valueOf(albumId));
        if (bitmap == null){
            bitmap = getAlbumArtFromFile(song);
        }
        return bitmap;
    }

    /**
     * 从媒体库读取专辑图片
     * @param context
     * @param album_id
     * @return
     */
    private Bitmap getAlbumArt(Context context,String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + album_id),  projection, null, null, null);
        String albumArt = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0)
        {  cur.moveToNext();
            albumArt = cur.getString(0);
        }
        cur.close();

        if (TextUtils.isEmpty(albumArt)){
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(albumArt);
        return bitmap;
    }

    /**
     * 从文件中读取专辑图片
     * @param song
     * @return
     */
    private Bitmap getAlbumArtFromFile(Song song){
        LogUtil.i(TAG,"getAlbumArtFromFile");
        File songFile = new File(song.getPath());
        if (songFile.exists()){
            String parentPath = songFile.getParentFile().getParent();
            String name = song.getAlbum() + "_" + song.getArtist();
            String albumPath = parentPath + File.separator + ALBUM_DIR + File.separator + name + ALBUM_SUFFIX;
            File albumFile = new File(albumPath);
            if (albumFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(albumPath);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 将歌曲进行排序
     */
    protected void sortList(){
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                if (lhs.getTitle().length() > 0 && rhs.getTitle().length() > 0) {
                    //如果两者都是汉字开头,则获取每个汉字的首字母
                    if (StringUtil.isChinese(lhs.getTitle().charAt(0)) && StringUtil.isChinese(rhs.getTitle().charAt(0))) {
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
        void onScanFinish();
    }
}
