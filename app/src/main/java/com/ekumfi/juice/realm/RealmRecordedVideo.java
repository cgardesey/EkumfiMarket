package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmRecordedVideo extends RealmObject {

    private int id;
    @PrimaryKey
    private String videoid;
    private String instructorcourseid;
    private String title;
    private String description;
    private String url;
    private String wowzalink;
    private String gsmcalllink;
    private String giflink;
    private String thumbnail;
    private int active;
    private String source;
    private String created_at;
    private String updated_at;

    public RealmRecordedVideo() {

    }

    public RealmRecordedVideo(String videoid, String instructorcourseid, String title, String description, String url, String wowzalink, String gsmcalllink, String giflink, String thumbnail, int active, String source, String created_at, String updated_at) {
        this.videoid = videoid;
        this.instructorcourseid = instructorcourseid;
        this.title = title;
        this.description = description;
        this.url = url;
        this.wowzalink = wowzalink;
        this.gsmcalllink = gsmcalllink;
        this.giflink = giflink;
        this.thumbnail = thumbnail;
        this.active = active;
        this.source = source;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWowzalink() {
        return wowzalink;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setWowzalink(String wowzalink) {
        this.wowzalink = wowzalink;
    }

    public String getGsmcalllink() {
        return gsmcalllink;
    }

    public void setGsmcalllink(String gsmcalllink) {
        this.gsmcalllink = gsmcalllink;
    }

    public String getGiflink() {
        return giflink;
    }

    public void setGiflink(String giflink) {
        this.giflink = giflink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
