package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmConsumer extends RealmObject {

    private int id;
    @PrimaryKey
    private String consumer_id;
    private String confirmation_token;
    private String name;
    private String profile_image_url;
    private String gender;
    private String employment_category;
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

    public RealmConsumer() {

    }

    public RealmConsumer(int id, String consumer_id, String confirmation_token, String name, String profile_image_url, String gender, String employment_category, String primary_contact, String auxiliary_contact, double longitude, double latitude, String digital_address, String street_address, String user_id, String created_at, String updated_at, RealmUser user) {
        this.id = id;
        this.consumer_id = consumer_id;
        this.confirmation_token = confirmation_token;
        this.name = name;
        this.profile_image_url = profile_image_url;
        this.gender = gender;
        this.employment_category = employment_category;
        this.primary_contact = primary_contact;
        this.auxiliary_contact = auxiliary_contact;
        this.longitude = longitude;
        this.latitude = latitude;
        this.digital_address = digital_address;
        this.street_address = street_address;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmployment_category() {
        return employment_category;
    }

    public void setEmployment_category(String employment_category) {
        this.employment_category = employment_category;
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
