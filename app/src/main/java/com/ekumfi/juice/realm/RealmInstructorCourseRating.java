package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmInstructorCourseRating extends RealmObject {

    private int id;
    @PrimaryKey
    private String instructorcourseratingid;
    private String studentid;
    private String instructorcourseid;
    private int onestar;
    private int twostar;
    private int threestar;
    private int fourstar;
    private int fivestar;
    private String review;
    private String created_at;
    private String updated_at;

    public RealmInstructorCourseRating() {

    }

    public RealmInstructorCourseRating(String instructorcourseratingid, String studentid, String instructorcourseid, int onestar, int twostar, int threestar, int fourstar, int fivestar, String review, String created_at, String updated_at) {
        this.instructorcourseratingid = instructorcourseratingid;
        this.studentid = studentid;
        this.instructorcourseid = instructorcourseid;
        this.onestar = onestar;
        this.twostar = twostar;
        this.threestar = threestar;
        this.fourstar = fourstar;
        this.fivestar = fivestar;
        this.review = review;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstructorcourseratingid() {
        return instructorcourseratingid;
    }

    public void setInstructorcourseratingid(String instructorcourseratingid) {
        this.instructorcourseratingid = instructorcourseratingid;
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

    public int getOnestar() {
        return onestar;
    }

    public void setOnestar(int onestar) {
        this.onestar = onestar;
    }

    public int getTwostar() {
        return twostar;
    }

    public void setTwostar(int twostar) {
        this.twostar = twostar;
    }

    public int getThreestar() {
        return threestar;
    }

    public void setThreestar(int threestar) {
        this.threestar = threestar;
    }

    public int getFourstar() {
        return fourstar;
    }

    public void setFourstar(int fourstar) {
        this.fourstar = fourstar;
    }

    public int getFivestar() {
        return fivestar;
    }

    public void setFivestar(int fivestar) {
        this.fivestar = fivestar;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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
