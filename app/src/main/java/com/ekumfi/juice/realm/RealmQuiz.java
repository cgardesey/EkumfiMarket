package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmQuiz extends RealmObject {

    private int id;
    @PrimaryKey
    private String quizid;
    private String instructorcourseid;
    private String title;
    private String description;
    private String starttime;
    private String endtime;
    private String date;
    private String url;
    private String question;
    private String created_at;
    private String updated_at;

    private String percentagescore;

    public RealmQuiz() {

    }

    public RealmQuiz(String quizid, String instructorcourseid, String title, String description, String starttime, String endtime, String date, String url, String question, String created_at, String updated_at) {
        this.quizid = quizid;
        this.instructorcourseid = instructorcourseid;
        this.title = title;
        this.description = description;
        this.starttime = starttime;
        this.endtime = endtime;
        this.date = date;
        this.url = url;
        this.question = question;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public String getPercentagescore() {
        return percentagescore;
    }

    public void setPercentagescore(String percentagescore) {
        this.percentagescore = percentagescore;
    }
}
