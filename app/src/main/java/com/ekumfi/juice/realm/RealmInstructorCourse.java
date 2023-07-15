package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmInstructorCourse extends RealmObject {

    private int id;
    @PrimaryKey
    private String instructorcourseid;
    private String instructorid;
    private String courseid;
    private String institutionid;
    private String price_day;
    private String price_week;
    private String price;
    private int fee_type_id;
    private String currency;
    private String room_number;
    private String nodeserver;
    private int total_ratings;
    private float rating;
    private int connectedtoaudio;
    private int connectedtovideo;
    private int connectedtocall;
    private int connectedtochat;
    private String created_at;
    private String updated_at;

    private String picture;
    private String name;
    private String edubackground;
    private String about;
    private String institution;
    private String coursepath;


    public RealmInstructorCourse() {

    }

    public RealmInstructorCourse(String instructorcourseid, String instructorid, String courseid, String institutionid, String price_day, String price_week, String price, int fee_type_id, String currency, String room_number, String nodeserver, int total_ratings, float rating, int connectedtoaudio, int connectedtovideo, int connectedtocall, int connectedtochat, String created_at, String updated_at) {
        this.instructorcourseid = instructorcourseid;
        this.instructorid = instructorid;
        this.courseid = courseid;
        this.institutionid = institutionid;
        this.price_day = price_day;
        this.price_week = price_week;
        this.price = price;
        this.fee_type_id = fee_type_id;
        this.currency = currency;
        this.room_number = room_number;
        this.nodeserver = nodeserver;
        this.total_ratings = total_ratings;
        this.rating = rating;
        this.connectedtoaudio = connectedtoaudio;
        this.connectedtovideo = connectedtovideo;
        this.connectedtocall = connectedtocall;
        this.connectedtochat = connectedtochat;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public String getInstructorid() {
        return instructorid;
    }

    public void setInstructorid(String instructorid) {
        this.instructorid = instructorid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(String institutionid) {
        this.institutionid = institutionid;
    }

    public String getPrice_day() {
        return price_day;
    }

    public void setPrice_day(String price_day) {
        this.price_day = price_day;
    }

    public String getPrice_week() {
        return price_week;
    }

    public void setPrice_week(String price_week) {
        this.price_week = price_week;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getFee_type_id() {
        return fee_type_id;
    }

    public void setFee_type_id(int fee_type_id) {
        this.fee_type_id = fee_type_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getNodeserver() {
        return nodeserver;
    }

    public void setNodeserver(String nodeserver) {
        this.nodeserver = nodeserver;
    }

    public int getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(int total_ratings) {
        this.total_ratings = total_ratings;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getConnectedtoaudio() {
        return connectedtoaudio;
    }

    public void setConnectedtoaudio(int connectedtoaudio) {
        this.connectedtoaudio = connectedtoaudio;
    }

    public int getConnectedtovideo() {
        return connectedtovideo;
    }

    public void setConnectedtovideo(int connectedtovideo) {
        this.connectedtovideo = connectedtovideo;
    }

    public int getConnectedtocall() {
        return connectedtocall;
    }

    public void setConnectedtocall(int connectedtocall) {
        this.connectedtocall = connectedtocall;
    }

    public int getConnectedtochat() {
        return connectedtochat;
    }

    public void setConnectedtochat(int connectedtochat) {
        this.connectedtochat = connectedtochat;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEdubackground() {
        return edubackground;
    }

    public void setEdubackground(String edubackground) {
        this.edubackground = edubackground;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCoursepath() {
        return coursepath;
    }

    public void setCoursepath(String coursepath) {
        this.coursepath = coursepath;
    }
}
