package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmQuestionBank extends RealmObject {
    @PrimaryKey
    private String quizid;
    private String courseid,title,numberofquestions,datecreated,dateoftest,starttime,endtime,description,url;

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

    public RealmQuestionBank() {
    }

    public RealmQuestionBank(String quizid, String courseid, String title, String numberofquestions, String datecreated,
                             String dateoftest, String starttime, String endtime, String description, String url) {
        this.quizid = quizid;
        this.courseid = courseid;
        this.title = title;
        this.numberofquestions = numberofquestions;
        this.datecreated = datecreated;
        this.dateoftest = dateoftest;
        this.starttime = starttime;
        this.endtime = endtime;
        this.description = description;
        this.url = url;
    }

    public RealmQuestionBank(String quizid, String courseid, String title, String numberofquestions, String datecreated, String dateoftest) {
        this.quizid = quizid;
        this.courseid = courseid;
        this.title = title;
        this.numberofquestions = numberofquestions;
        this.datecreated = datecreated;
        this.dateoftest = dateoftest;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = title;
    }

    public String getNumberofquestions() {
        return numberofquestions;
    }

    public void setNumberofquestions(String numberofquestions) {
        this.numberofquestions = numberofquestions;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getDateoftest() {
        return dateoftest;
    }

    public void setDateoftest(String dateoftest) {
        this.dateoftest = dateoftest;
    }
}
