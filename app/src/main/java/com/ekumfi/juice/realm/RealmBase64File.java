package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmBase64File extends RealmObject {

    @PrimaryKey
    private String realmbase64fileid;
    private int id;
    private String url;
    private String base64String;

    public RealmBase64File() {
    }

    public RealmBase64File(String realmbase64fileid, int id, String url, String base64String) {
        this.realmbase64fileid = realmbase64fileid;
        this.id = id;
        this.url = url;
        this.base64String = base64String;
    }

    public String getRealmbase64fileid() {
        return realmbase64fileid;
    }

    public void setRealmbase64fileid(String realmbase64fileid) {
        this.realmbase64fileid = realmbase64fileid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }
}
