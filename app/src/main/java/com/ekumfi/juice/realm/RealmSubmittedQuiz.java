package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmSubmittedQuiz extends RealmObject {

    private int id;
    @PrimaryKey
    private String submittedquizid;
    private String title;
    private String url;
    private int percentagescore;
    private String quizid;
    private String studentid;
    private String created_at;
    private String updated_at;

    public RealmSubmittedQuiz() {

    }

    public RealmSubmittedQuiz(String submittedquizid, String title, String url, int percentagescore, String quizid, String studentid, String created_at, String updated_at) {
        this.submittedquizid = submittedquizid;
        this.title = title;
        this.url = url;
        this.percentagescore = percentagescore;
        this.quizid = quizid;
        this.studentid = studentid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubmittedquizid() {
        return submittedquizid;
    }

    public void setSubmittedquizid(String submittedquizid) {
        this.submittedquizid = submittedquizid;
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

    public int getPercentagescore() {
        return percentagescore;
    }

    public void setPercentagescore(int percentagescore) {
        this.percentagescore = percentagescore;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
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
