package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmEkumfiInfo extends RealmObject {

    private int id;
    @PrimaryKey
    private String ekumfi_info_id;
    private String name;
    private String profile_image_url;
    private String primary_contact;
    private String auxiliary_contact;
    private double longitude;
    private double latitude;
    private String digital_address;
    private String street_address;
    private String availability;
    private String created_at;
    private String updated_at;

    public RealmEkumfiInfo() {

    }

    public RealmEkumfiInfo(int id, String ekumfi_info_id, String name, String profile_image_url, String primary_contact, String auxiliary_contact, double longitude, double latitude, String digital_address, String street_address, String availability, String created_at, String updated_at) {
        this.id = id;
        this.ekumfi_info_id = ekumfi_info_id;
        this.name = name;
        this.profile_image_url = profile_image_url;
        this.primary_contact = primary_contact;
        this.auxiliary_contact = auxiliary_contact;
        this.longitude = longitude;
        this.latitude = latitude;
        this.digital_address = digital_address;
        this.street_address = street_address;
        this.availability = availability;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEkumfi_info_id() {
        return ekumfi_info_id;
    }

    public void setEkumfi_info_id(String ekumfi_info_id) {
        this.ekumfi_info_id = ekumfi_info_id;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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
