package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmServiceCategory extends RealmObject {

    private int id;
    @PrimaryKey
    private String service_category_id;
    private String title;
    private String description;
    private String url;
    private String tag;

    public RealmServiceCategory() {

    }

    public RealmServiceCategory(int id, String service_category_id, String title, String description, String url, String tag) {
        this.id = id;
        this.service_category_id = service_category_id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_category_id() {
        return service_category_id;
    }

    public void setService_category_id(String service_category_id) {
        this.service_category_id = service_category_id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
