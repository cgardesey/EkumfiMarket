package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmNotification extends RealmObject {

    private int id;
    @PrimaryKey
    private String notificationid;
    private String text;
    private int readbyrecepient;
    private String senderid;
    private String created_at;
    private String updated_at;


    public RealmNotification() {

    }

    public RealmNotification(String notificationid, String text, int readbyrecepient, String senderid, String created_at, String updated_at) {
        this.notificationid = notificationid;
        this.text = text;
        this.readbyrecepient = readbyrecepient;
        this.senderid = senderid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReadbyrecepient() {
        return readbyrecepient;
    }

    public void setReadbyrecepient(int readbyrecepient) {
        this.readbyrecepient = readbyrecepient;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
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
