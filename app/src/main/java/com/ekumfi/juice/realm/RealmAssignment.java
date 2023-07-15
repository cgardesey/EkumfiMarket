package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmAssignment extends RealmObject {

    private int id;
    @PrimaryKey
    private String assignmentid;
    private String title;
    private String url;
    private String instructorcourseid;
    private String submitdate;
    private String created_at;
    private String updated_at;

    private String coursepath;
    private int submitted;
    private String submittedurl;
    private String submittedtitle;
    private int score;
    private String markedassignmenturl;
    private String enrolmentid;

    public RealmAssignment() {

    }

    public RealmAssignment(String assignmentid, String title, String url, String instructorcourseid, String submitdate, String created_at, String updated_at) {
        this.assignmentid = assignmentid;
        this.title = title;
        this.url = url;
        this.instructorcourseid = instructorcourseid;
        this.submitdate = submitdate;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignmentid() {
        return assignmentid;
    }

    public void setAssignmentid(String assignmentid) {
        this.assignmentid = assignmentid;
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

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
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

    public String getSubmitdate() {
        return submitdate;
    }

    public void setSubmitdate(String submitdate) {
        this.submitdate = submitdate;
    }

    public String getCoursepath() {
        return coursepath;
    }

    public void setCoursepath(String coursepath) {
        this.coursepath = coursepath;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public String getSubmittedurl() {
        return submittedurl;
    }

    public void setSubmittedurl(String submittedurl) {
        this.submittedurl = submittedurl;
    }

    public String getSubmittedtitle() {
        return submittedtitle;
    }

    public void setSubmittedtitle(String submittedtitle) {
        this.submittedtitle = submittedtitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMarkedassignmenturl() {
        return markedassignmenturl;
    }

    public void setMarkedassignmenturl(String markedassignmenturl) {
        this.markedassignmenturl = markedassignmenturl;
    }

    public String getEnrolmentid() {
        return enrolmentid;
    }

    public void setEnrolmentid(String enrolmentid) {
        this.enrolmentid = enrolmentid;
    }
}
