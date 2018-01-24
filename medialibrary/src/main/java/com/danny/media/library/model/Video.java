package com.danny.media.library.model;

import java.io.Serializable;

/**
 * Created by dannywang on 2017/12/29.
 */

public class Video implements Serializable {
    private long id;
    private String title;
    private String fileName;
    private String path;
    private long duration;
    private long fileSize;
    private int width;
    private int height;
    private String thumbnail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Video && (getId() == ((Video) obj).getId());
    }

    @Override
    public String toString() {
        String temp = "id : " + id + " , title : " + title + " , fileName : " + fileName + " , path : " + path + " , fileSize : " + fileSize
                + " , duration : " + duration + " , width : " + width + " , height : " + height + " , thumbnail : " + thumbnail;
        return temp;
    }
}
