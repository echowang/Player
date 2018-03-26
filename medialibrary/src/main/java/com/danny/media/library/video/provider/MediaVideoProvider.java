package com.danny.media.library.video.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.danny.media.library.video.model.Video;
import com.danny.media.library.utils.LogUtil;

/**
 * Created by tingw on 2018/1/23.
 */

public class MediaVideoProvider extends VideoProvider {
    private final static String TAG = MediaVideoProvider.class.getSimpleName();

    public MediaVideoProvider(Context context) {
        super(context);
    }

    @Override
    public void queryMediaResources() {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columnArgs = new String[]{MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT};
        String[] thumbColumns = new String[]{
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID
        };
        final Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Video video = new Video();
                video.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)));
                video.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                video.setFileName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)));
                video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
                video.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
                video.setFileSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)));
                video.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)));
                video.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)));

                String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
                String[] selectionArgs = new String[]{video.getId()+""};
                Cursor thumbCursor = contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
                if (thumbCursor != null && thumbCursor.moveToNext()){
                    video.setThumbnail(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }

                LogUtil.i(TAG,video.toString());
                videoList.add(video);
            }

            cursor.close();
        }
    }
}
