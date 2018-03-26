package com.danny.media.library.music.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by tingw on 2018/1/2.
 */

public class Lrc implements Serializable {
    public enum LrcType {
        LOCAL,
        ONLINE
    }

    public final static String SUFFIX = ".lrc";
    public final static String DIR = "lyric";

    private LrcType type;
    //本地歌词文件路径
    private File lrcFile;
    //网络歌词文件
    private String lrcContent;

    public LrcType getType() {
        return type;
    }

    public void setType(LrcType type) {
        this.type = type;
    }

    public File getLrcFile() {
        return lrcFile;
    }

    public void setLrcFile(File lrcFile) {
        this.lrcFile = lrcFile;
    }

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }


}
