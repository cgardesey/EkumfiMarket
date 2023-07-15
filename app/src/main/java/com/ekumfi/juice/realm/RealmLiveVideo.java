package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmLiveVideo extends RealmObject {

    private int id;
    @PrimaryKey
    private String livevideoid;
    private String instructorcourseid;
    private String title;
    private String description;
    private String url;
    private String giflink;
    private String thumbnail;
    private int isactive;
    private String created_at;
    private String updated_at;

    public RealmLiveVideo() {

    }

    public RealmLiveVideo(String livevideoid, String instructorcourseid, String title, String description, String url, String giflink, String thumbnail, int isactive, String created_at, String updated_at) {
        this.livevideoid = livevideoid;
        this.instructorcourseid = instructorcourseid;
        this.title = title;
        this.description = description;
        this.url = url;
        this.giflink = giflink;
        this.thumbnail = thumbnail;
        this.isactive = isactive;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLivevideoid() {
        return livevideoid;
    }

    public void setLivevideoid(String livevideoid) {
        this.livevideoid = livevideoid;
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

    public void setDescription(String description) {
        this.description = description;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
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
