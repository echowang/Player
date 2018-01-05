package com.danny.media.library.file;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.danny.media.library.model.Song;
import com.danny.media.library.utils.Util;

/**
 * Created by tingw on 2018/1/2.
 * 通过媒体库查询音频信息
 */

public class MediaMusicProvider extends MusicProvider {
    public static final String DEFAULT_TITLE = "未知歌曲";
    public static final String DEFAULT_ALBUM = "未知专辑";
    public static final String DEFAULT_SINGER = "未知歌手";

    public MediaMusicProvider(Context context) {
        super(context);
    }

    @Override
    protected void scanMediaResources() {
        //查询媒体库数据
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,                    //0歌曲ID
                        MediaStore.Audio.Media.TITLE,                       //1歌曲的名称
                        MediaStore.Audio.Media.ALBUM,                       //2歌曲的专辑名
                        MediaStore.Audio.Media.ARTIST,                      //3歌曲的歌手名
                        MediaStore.Audio.Media.DATA,                        //4歌曲文件的全路径
                        MediaStore.Audio.Media.DISPLAY_NAME,                //5歌曲文件的名称
                        MediaStore.Audio.Media.YEAR,                        //6歌曲文件的发行日期
                        MediaStore.Audio.Media.DURATION,                    //7歌曲的总播放时长
                        MediaStore.Audio.Media.SIZE,                        //8歌曲文件的大小
                        MediaStore.Audio.Media.ALBUM_ID},                   //9专辑ID
                null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            do {
                Song song = new Song();
                song.setType(Song.Type.LOCAL);
                song.setId(cursor.getLong(0));
                song.setTitle(cursor.getString(1));
                song.setAlbum(cursor.getString(2));
                song.setArtist(cursor.getString(3));
                song.setPath(cursor.getString(4));
                song.setFileName(cursor.getString(5));
                song.setIssueDate(cursor.getString(6));
                song.setDuration(cursor.getInt(7));
                song.setFileSize(cursor.getLong(8));
                song.setAlbumId(cursor.getLong(9));

                song.setAlbum(song.getAlbum().replace("\u007F",""));
                song.setArtist(song.getArtist().replace("<unknown>",DEFAULT_SINGER));

                if (Util.isGarbledCode(song.getTitle()) || Util.isGarbledCode(song.getArtist()) || Util.isGarbledCode(song.getAlbum())) {
                    if (song.getFileName() != null && song.getFileName().contains("-")) {
                        String[] split = song.getFileName().split("-");
                        String[] split1 = split[1].split("\\.");
                        song.setTitle(split1[0]);
                        song.setArtist(split[0]);
                        song.setAlbum(DEFAULT_ALBUM);
                    }
                }
                songList.add(song);
            }while (cursor.moveToNext());

            if (!cursor.isClosed()){
                cursor.close();
            }
        }
    }
}
