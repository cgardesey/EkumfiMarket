package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmTimetable extends RealmObject {

    private int id;
    @PrimaryKey
    private String timetableid;
    private String dow;
    private int period_id;
    private String instructorcourseid;
    private String created_at;
    private String updated_at;

    private String course;
    private String instructorname;
    private String about;
    private String starttime;
    private String endtime;
    private String instructorpic;
    private int downum;

    public RealmTimetable() {

    }

    public RealmTimetable(String timetableid, String dow, int period_id, String instructorcourseid, String created_at, String updated_at) {
        this.timetableid = timetableid;
        this.dow = dow;
        this.period_id = period_id;
        this.instructorcourseid = instructorcourseid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimetableid() {
        return timetableid;
    }

    public void setTimetableid(String timetableid) {
        this.timetableid = timetableid;
    }

    public String getDow() {
        return dow;
    }

    public void setDow(String dow) {
        this.dow = dow;
    }

    public int getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(int period_id) {
        this.period_id = period_id;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInstructorname() {
        return instructorname;
    }

    public void setInstructorname(String instructorname) {
        this.instructorname = instructorname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public String getInstructorpic() {
        return instructorpic;
    }

    public void setInstructorpic(String instructorpic) {
        this.instructorpic = instructorpic;
    }

    public int getDownum() {
        return downum;
    }

    public void setDownum(int downum) {
        this.downum = downum;
    }
}
