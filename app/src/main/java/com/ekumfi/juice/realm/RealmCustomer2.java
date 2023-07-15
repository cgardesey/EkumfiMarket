package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmCustomer2 extends RealmObject {

    private int id;
    @PrimaryKey
    private String customer_id;
    private String confirmation_token;
        private String profile_image_url;
    private String name;
    private String gender;
    private String primary_contact;
    private String auxiliary_contact;
    private double longitude;
    private double latitude;
    private String digital_address;
    private String street_address;
    private String user_id;
    private String created_at;
    private String updated_at;

    private RealmUser user;

    public RealmCustomer2() {

    }

    public RealmCustomer2(int id, String customer_id, String confirmation_token, String profile_image_url, String name, String gender, String primary_contact, String auxiliary_contact, double longitude, double latitude, String digital_address, String street_address, String user_id, String created_at, String updated_at) {
        this.id = id;
        this.customer_id = customer_id;
        this.confirmation_token = confirmation_token;
        this.profile_image_url = profile_image_url;
        this.name = name;
        this.gender = gender;
        this.primary_contact = primary_contact;
        this.auxiliary_contact = auxiliary_contact;
        this.longitude = longitude;
        this.latitude = latitude;
        this.digital_address = digital_address;
        this.street_address = street_address;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(String primary_contact) {
        this.primary_contact = primary_contact;
    }

    public String getAuxiliary_contact() {
        return auxiliary_contact;
    }

    public void setAuxiliary_contact(String auxiliary_contact) {
        this.auxiliary_contact = auxiliary_contact;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDigital_address() {
        return digital_address;
    }

    public void setDigital_address(String digital_address) {
        this.digital_address = digital_address;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public RealmUser getUser() {
        return user;
    }

    public void setUser(RealmUser user) {
        this.user = user;
    }
}
