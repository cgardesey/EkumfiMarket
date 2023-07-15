package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmIdentificationType extends RealmObject {

    private int id;
    @PrimaryKey
    private String identification_type_id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;

    public RealmIdentificationType() {

    }

    public RealmIdentificationType(String identification_type_id, String name, String description, String created_at, String updated_at) {
        this.identification_type_id = identification_type_id;
        this.name = name;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification_type_id() {
        return identification_type_id;
    }

    public void setIdentification_type_id(String identification_type_id) {
        this.identification_type_id = identification_type_id;
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
