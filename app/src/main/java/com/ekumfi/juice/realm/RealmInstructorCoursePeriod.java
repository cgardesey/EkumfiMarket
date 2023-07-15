package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmInstructorCoursePeriod extends RealmObject {

    private int id;
    @PrimaryKey
    private String instructorcourseperiodid;
    private String instructorcourseid;
    private String periodid;
    private String created_at;
    private String updated_at;

    public RealmInstructorCoursePeriod() {

    }

    public RealmInstructorCoursePeriod(String instructorcourseperiodid, String instructorcourseid, String periodid, String created_at, String updated_at) {
        this.instructorcourseperiodid = instructorcourseperiodid;
        this.instructorcourseid = instructorcourseid;
        this.periodid = periodid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstructorcourseperiodid() {
        return instructorcourseperiodid;
    }

    public void setInstructorcourseperiodid(String instructorcourseperiodid) {
        this.instructorcourseperiodid = instructorcourseperiodid;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public String getPeriodid() {
        return periodid;
    }

    public void setPeriodid(String periodid) {
        this.periodid = periodid;
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
