package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmProviderEduCert extends RealmObject {

    private int id;
    @PrimaryKey
    private String provider_edu_cert_id;
    private String cert_title;
    private String institution_name;
    private String start_date;
    private String end_date;
    private String provider_id;
    private String created_at;
    private String updated_at;

    public RealmProviderEduCert() {

    }

    public RealmProviderEduCert(String provider_edu_cert_id, String cert_title, String institution_name, String start_date, String end_date, String provider_id, String created_at, String updated_at) {
        this.provider_edu_cert_id = provider_edu_cert_id;
        this.cert_title = cert_title;
        this.institution_name = institution_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.provider_id = provider_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvider_edu_cert_id() {
        return provider_edu_cert_id;
    }

    public void setProvider_edu_cert_id(String provider_edu_cert_id) {
        this.provider_edu_cert_id = provider_edu_cert_id;
    }

    public String getCert_title() {
        return cert_title;
    }

    public void setCert_title(String cert_title) {
        this.cert_title = cert_title;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public void setInstitution_name(String institution_name) {
        this.institution_name = institution_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
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
