package com.ekumfi.juice.pojo;

/**
 * Created by Andy on 2/25/2020.
 */

public class MyFile {
    String path, url, giflink;

    public MyFile() {
    }

    public MyFile(String path, String url, String giflink) {
        this.path = path;
        this.url = url;
        this.giflink = giflink;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGiflink() {
        return giflink;
    }

    public void setGiflink(String giflink) {
        this.giflink = giflink;
    }
}
