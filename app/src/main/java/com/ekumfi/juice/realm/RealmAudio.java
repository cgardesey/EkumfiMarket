package com.ekumfi.juice.realm;

import com.ekumfi.juice.constants.Const;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmAudio extends RealmObject implements Comparable<RealmAudio> {

    private int id;
    @PrimaryKey
    private String audioid;
    private String sessionid;
    private String title;
    private String url;
    private String audiourl;
    private String gsmcalllink;
    private String wowzalink;
    private String instructorcourseid;
    private String giflink;
    private String thumbnail;
    private int active;
    private String created_at;
    private String updated_at;

    private boolean attended;
    private String coursepath;

    public RealmAudio() {

    }

    public RealmAudio(String audioid, String sessionid, String title, String url, String audiourl, String gsmcalllink, String wowzalink, String instructorcourseid, String giflink, String thumbnail, int active, String created_at, String updated_at) {
        this.audioid = audioid;
        this.sessionid = sessionid;
        this.title = title;
        this.url = url;
        this.audiourl = audiourl;
        this.gsmcalllink = gsmcalllink;
        this.wowzalink = wowzalink;
        this.instructorcourseid = instructorcourseid;
        this.giflink = giflink;
        this.thumbnail = thumbnail;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudioid() {
        return audioid;
    }

    public void setAudioid(String audioid) {
        this.audioid = audioid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getGsmcalllink() {
        return gsmcalllink;
    }

    public void setGsmcalllink(String gsmcalllink) {
        this.gsmcalllink = gsmcalllink;
    }

    public String getWowzalink() {
        return wowzalink;
    }

    public void setWowzalink(String wowzalink) {
        this.wowzalink = wowzalink;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
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

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public String getCoursepath() {
        return coursepath;
    }

    public void setCoursepath(String coursepath) {
        this.coursepath = coursepath;
    }

    @Override
    public int compareTo(RealmAudio realmAudio) {
        try {
            if (Const.dateTimeFormat.parse(created_at).getTime() > Const.dateTimeFormat.parse(realmAudio.getCreated_at()).getTime()) {
                return 1;
            } else if (Const.dateTimeFormat.parse(created_at).getTime() < Const.dateTimeFormat.parse(realmAudio.getCreated_at()).getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
