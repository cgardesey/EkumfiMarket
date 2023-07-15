package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmServiceImage extends RealmObject {

    private int id;
    @PrimaryKey
    private String service_image_id;
    private String url;
    private String name;
    private String description;
    private int featured_image;
    private String service_id;
    private String created_at;
    private String updated_at;

    public RealmServiceImage() {

    }

    public RealmServiceImage(String service_image_id, String url, String name, String description, int featured_image, String service_id, String created_at, String updated_at) {
        this.service_image_id = service_image_id;
        this.url = url;
        this.name = name;
        this.description = description;
        this.featured_image = featured_image;
        this.service_id = service_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_image_id() {
        return service_image_id;
    }

    public void setService_image_id(String service_image_id) {
        this.service_image_id = service_image_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(int featured_image) {
        this.featured_image = featured_image;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
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
