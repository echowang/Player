package com.danny.media.library.model;

import java.io.Serializable;

/**
 * Created by tingw on 2018/1/2.
 */

public class Lrc implements Serializable {
    private String lrcContent;

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }
}
