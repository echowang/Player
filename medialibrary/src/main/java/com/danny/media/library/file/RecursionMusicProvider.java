package com.danny.media.library.file;

import android.content.Context;

/**
 * Created by tingw on 2018/1/2.
 * 递归查询SD内的音频文件，查询音频信息
 */

public class RecursionMusicProvider extends MusicProvider {

    public RecursionMusicProvider(Context context) {
        super(context);
    }

    @Override
    public void scanMediaResources() {

    }
}
