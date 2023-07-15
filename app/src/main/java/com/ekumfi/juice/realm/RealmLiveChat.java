package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmLiveChat extends RealmObject {

    private int id;
    @PrimaryKey
    private String livechatid;
    private String chatrefid;
    private String tempid;
    private String text;
    private String link;
    private String linktitle;
    private String linkdescription;
    private String linkimage;
    private String attachmenturl;
    private String attachmenttype;
    private String attachmenttitle;
    private int readbyrecepient;
    private String instructorcourseid;
    private String senderid;
    private String recepientid;
    private String created_at;
    private String updated_at;

    private String userid;
    private String name;
    private String sendername;
    private String recepientname;
    private String picture;
    private String senderpicture;
    private String recepientpicture;
    private String replyname;
    private String replybody;
    private boolean instructor;


    public RealmLiveChat() {

    }

    public RealmLiveChat(String chatid, String chatrefid, String tempid, String text, String link, String linktitle, String linkdescription, String linkimage, String attachmenturl, String attachmenttype, String attachmenttitle, int readbyrecepient, String instructorcourseid, String senderid, String recepientid, String created_at, String updated_at) {
        this.livechatid = chatid;
        this.chatrefid = chatrefid;
        this.tempid = tempid;
        this.text = text;
        this.link = link;
        this.linktitle = linktitle;
        this.linkdescription = linkdescription;
        this.linkimage = linkimage;
        this.attachmenturl = attachmenturl;
        this.attachmenttype = attachmenttype;
        this.attachmenttitle = attachmenttitle;
        this.readbyrecepient = readbyrecepient;
        this.instructorcourseid = instructorcourseid;
        this.senderid = senderid;
        this.recepientid = recepientid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLivechatid() {
        return livechatid;
    }

    public void setLivechatid(String livechatid) {
        this.livechatid = livechatid;
    }

    public String getChatrefid() {
        return chatrefid;
    }

    public void setChatrefid(String chatrefid) {
        this.chatrefid = chatrefid;
    }

    public String getTempid() {
        return tempid;
    }

    public void setTempid(String tempid) {
        this.tempid = tempid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinktitle() {
        return linktitle;
    }

    public void setLinktitle(String linktitle) {
        this.linktitle = linktitle;
    }

    public String getLinkdescription() {
        return linkdescription;
    }

    public void setLinkdescription(String linkdescription) {
        this.linkdescription = linkdescription;
    }

    public String getLinkimage() {
        return linkimage;
    }

    public void setLinkimage(String linkimage) {
        this.linkimage = linkimage;
    }

    public String getAttachmenturl() {
        return attachmenturl;
    }

    public void setAttachmenturl(String attachmenturl) {
        this.attachmenturl = attachmenturl;
    }

    public String getAttachmenttype() {
        return attachmenttype;
    }

    public void setAttachmenttype(String attachmenttype) {
        this.attachmenttype = attachmenttype;
    }

    public String getAttachmenttitle() {
        return attachmenttitle;
    }

    public void setAttachmenttitle(String attachmenttitle) {
        this.attachmenttitle = attachmenttitle;
    }

    public int getReadbyrecepient() {
        return readbyrecepient;
    }

    public void setReadbyrecepient(int readbyrecepient) {
        this.readbyrecepient = readbyrecepient;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getRecepientid() {
        return recepientid;
    }

    public void setRecepientid(String recepientid) {
        this.recepientid = recepientid;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getRecepientname() {
        return recepientname;
    }

    public void setRecepientname(String recepientname) {
        this.recepientname = recepientname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSenderpicture() {
        return senderpicture;
    }

    public void setSenderpicture(String senderpicture) {
        this.senderpicture = senderpicture;
    }

    public String getRecepientpicture() {
        return recepientpicture;
    }

    public void setRecepientpicture(String recepientpicture) {
        this.recepientpicture = recepientpicture;
    }

    public String getReplyname() {
        return replyname;
    }

    public void setReplyname(String replyname) {
        this.replyname = replyname;
    }

    public String getReplybody() {
        return replybody;
    }

    public void setReplybody(String replybody) {
        this.replybody = replybody;
    }

    public boolean isInstructor() {
        return instructor;
    }

    public void setInstructor(boolean instructor) {
        this.instructor = instructor;
    }
}
