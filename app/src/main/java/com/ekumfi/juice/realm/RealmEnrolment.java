package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmEnrolment extends RealmObject {
    private int id;
    @PrimaryKey
    private String enrolmentid;
    private String studentid;
    private String instructorcourseid;
    private int enrolled;
    private String enrolmentfeeexpirydate;
    private int percentagecompleted;
    private int connectedtoaudio;
    private int connectedtovideo;
    private int connectedtocall;
    private int connectedtochat;
    private boolean enrolmentfeeexpired;
    private boolean appuserfeeexpired;
    private String appuserfeeexpirydate;
    private boolean institutionfeeexpired;
    private String institutionfeeexpirydate;
    private String created_at;
    private String updated_at;

    private String coursepath;
    private String dow;
    private String downum;
    private String starttime;
    private String endtime;
    private String time;
    private String classsessionid;
    private String instructorname;
    private String profilepicurl;
    private float rating;
    private int totalrating;
    private String price;
    private String price_day;
    private String price_week;
    private int fee_type_id;
    private String currency;
    private boolean live;
    private boolean upcoming;
    private boolean ratedbyme;
    private String dialcode;
    private String conferenceid;
    private String room_number;
    private String nodeserver;

    public RealmEnrolment() {

    }

    public RealmEnrolment(String enrolmentid, String studentid, String instructorcourseid, int enrolled, String enrolmentfeeexpirydate, int percentagecompleted, int connectedtoaudio, int connectedtovideo, int connectedtocall, int connectedtochat, boolean enrolmentfeeexpired, boolean appuserfeeexpired, String appuserfeeexpirydate, boolean institutionfeeexpired, String institutionfeeexpirydate, String created_at, String updated_at, String coursepath) {
        this.enrolmentid = enrolmentid;
        this.studentid = studentid;
        this.instructorcourseid = instructorcourseid;
        this.enrolled = enrolled;
        this.enrolmentfeeexpirydate = enrolmentfeeexpirydate;
        this.percentagecompleted = percentagecompleted;
        this.connectedtoaudio = connectedtoaudio;
        this.connectedtovideo = connectedtovideo;
        this.connectedtocall = connectedtocall;
        this.connectedtochat = connectedtochat;
        this.enrolmentfeeexpired = enrolmentfeeexpired;
        this.appuserfeeexpired = appuserfeeexpired;
        this.appuserfeeexpirydate = appuserfeeexpirydate;
        this.institutionfeeexpired = institutionfeeexpired;
        this.institutionfeeexpirydate = institutionfeeexpirydate;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.coursepath = coursepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnrolmentid() {
        return enrolmentid;
    }

    public void setEnrolmentid(String enrolmentid) {
        this.enrolmentid = enrolmentid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    public String getEnrolmentfeeexpirydate() {
        return enrolmentfeeexpirydate;
    }

    public void setEnrolmentfeeexpirydate(String enrolmentfeeexpirydate) {
        this.enrolmentfeeexpirydate = enrolmentfeeexpirydate;
    }

    public int getPercentagecompleted() {
        return percentagecompleted;
    }

    public void setPercentagecompleted(int percentagecompleted) {
        this.percentagecompleted = percentagecompleted;
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

    public boolean isEnrolmentfeeexpired() {
        return enrolmentfeeexpired;
    }

    public void setEnrolmentfeeexpired(boolean enrolmentfeeexpired) {
        this.enrolmentfeeexpired = enrolmentfeeexpired;
    }

    public boolean isAppuserfeeexpired() {
        return appuserfeeexpired;
    }

    public void setAppuserfeeexpired(boolean appuserfeeexpired) {
        this.appuserfeeexpired = appuserfeeexpired;
    }

    public String getAppuserfeeexpirydate() {
        return appuserfeeexpirydate;
    }

    public void setAppuserfeeexpirydate(String appuserfeeexpirydate) {
        this.appuserfeeexpirydate = appuserfeeexpirydate;
    }

    public boolean isInstitutionfeeexpired() {
        return institutionfeeexpired;
    }

    public void setInstitutionfeeexpired(boolean institutionfeeexpired) {
        this.institutionfeeexpired = institutionfeeexpired;
    }

    public String getInstitutionfeeexpirydate() {
        return institutionfeeexpirydate;
    }

    public void setInstitutionfeeexpirydate(String institutionfeeexpirydate) {
        this.institutionfeeexpirydate = institutionfeeexpirydate;
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

    public String getCoursepath() {
        return coursepath;
    }

    public void setCoursepath(String coursepath) {
        this.coursepath = coursepath;
    }

    public String getDow() {
        return dow;
    }

    public void setDow(String dow) {
        this.dow = dow;
    }

    public String getDownum() {
        return downum;
    }

    public void setDownum(String downum) {
        this.downum = downum;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClasssessionid() {
        return classsessionid;
    }

    public void setClasssessionid(String classsessionid) {
        this.classsessionid = classsessionid;
    }

    public String getInstructorname() {
        return instructorname;
    }

    public void setInstructorname(String instructorname) {
        this.instructorname = instructorname;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(int totalrating) {
        this.totalrating = totalrating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isUpcoming() {
        return upcoming;
    }

    public void setUpcoming(boolean upcoming) {
        this.upcoming = upcoming;
    }

    public boolean isRatedbyme() {
        return ratedbyme;
    }

    public void setRatedbyme(boolean ratedbyme) {
        this.ratedbyme = ratedbyme;
    }

    public String getDialcode() {
        return dialcode;
    }

    public void setDialcode(String dialcode) {
        this.dialcode = dialcode;
    }

    public String getConferenceid() {
        return conferenceid;
    }

    public void setConferenceid(String conferenceid) {
        this.conferenceid = conferenceid;
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
}
