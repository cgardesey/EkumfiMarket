package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmRecommendedDoc extends RealmObject {

    private int id;
    @PrimaryKey
    private String recommendeddocid;
    private String title;
    private String url;
    private String instructorcourseid;
    private String courseid;
    private String created_at;
    private String updated_at;

    public RealmRecommendedDoc() {

    }

    public RealmRecommendedDoc(String recommendeddocid, String title, String url, String instructorcourseid, String courseid, String created_at, String updated_at) {
        this.recommendeddocid = recommendeddocid;
        this.title = title;
        this.url = url;
        this.instructorcourseid = instructorcourseid;
        this.courseid = courseid;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecommendeddocid() {
        return recommendeddocid;
    }

    public void setRecommendeddocid(String recommendeddocid) {
        this.recommendeddocid = recommendeddocid;
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

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
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
