package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmSeller extends RealmObject {

    private int id;
    @PrimaryKey
    private String seller_id;
    private String confirmation_token;
    private String seller_type;
    private String shop_name;
    private String shop_image_url;
    private String primary_contact;
    private String auxiliary_contact;
    private String momo_number;
    private double longitude;
    private double latitude;
    private double live_longitude;
    private double live_latitude;
    private String digital_address;
    private String street_address;
    private String identification_type;
    private String identification_number;
    private String identification_image_url;
    private String availability;
    private String verified;
    private String user_id;
    private String agent_id;
    private String created_at;
    private String updated_at;

    public RealmSeller() {

    }

    public RealmSeller(int id, String seller_id, String confirmation_token, String seller_type, String shop_name, String shop_image_url, String primary_contact, String auxiliary_contact, String momo_number, double longitude, double latitude, double live_longitude, double live_latitude, String digital_address, String street_address, String identification_type, String identification_number, String identification_image_url, String availability, String verified, String user_id, String agent_id, String created_at, String updated_at) {
        this.id = id;
        this.seller_id = seller_id;
        this.confirmation_token = confirmation_token;
        this.seller_type = seller_type;
        this.shop_name = shop_name;
        this.shop_image_url = shop_image_url;
        this.primary_contact = primary_contact;
        this.auxiliary_contact = auxiliary_contact;
        this.momo_number = momo_number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.live_longitude = live_longitude;
        this.live_latitude = live_latitude;
        this.digital_address = digital_address;
        this.street_address = street_address;
        this.identification_type = identification_type;
        this.identification_number = identification_number;
        this.identification_image_url = identification_image_url;
        this.availability = availability;
        this.verified = verified;
        this.user_id = user_id;
        this.agent_id = agent_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getSeller_type() {
        return seller_type;
    }

    public void setSeller_type(String seller_type) {
        this.seller_type = seller_type;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image_url() {
        return shop_image_url;
    }

    public void setShop_image_url(String shop_image_url) {
        this.shop_image_url = shop_image_url;
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

    public String getMomo_number() {
        return momo_number;
    }

    public void setMomo_number(String momo_number) {
        this.momo_number = momo_number;
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

    public double getLive_longitude() {
        return live_longitude;
    }

    public void setLive_longitude(double live_longitude) {
        this.live_longitude = live_longitude;
    }

    public double getLive_latitude() {
        return live_latitude;
    }

    public void setLive_latitude(double live_latitude) {
        this.live_latitude = live_latitude;
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

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public String getIdentification_number() {
        return identification_number;
    }

    public void setIdentification_number(String identification_number) {
        this.identification_number = identification_number;
    }

    public String getIdentification_image_url() {
        return identification_image_url;
    }

    public void setIdentification_image_url(String identification_image_url) {
        this.identification_image_url = identification_image_url;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
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
