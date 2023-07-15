package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmProductCategory extends RealmObject {

    private int id;
    @PrimaryKey
    private String product_category_id;
    private String title;
    private String description;
    private String url;
    private String tag;

    public RealmProductCategory() {

    }

    public RealmProductCategory(int id, String product_category_id, String title, String description, String url, String tag) {
        this.id = id;
        this.product_category_id = product_category_id;
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

    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
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
