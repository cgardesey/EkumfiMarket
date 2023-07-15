package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmCartold extends RealmObject {

    private int id;
    @PrimaryKey
    private String cart_id;
    private String order_id;
    private String provider_id;
    private String customer_id;

    private String status;
    private double shipping_fee;

    private double longitude;
    private double latitude;
    private String provider_name;
    private String title;
    private String first_name;
    private String last_name;
    private String other_name;
    private int verified;
    private String profile_image_url;
    private int item_count;

    public RealmCartold() {

    }

    public RealmCartold(int id, String cart_id, String order_id, String provider_id, String customer_id, String status, double shipping_fee, double longitude, double latitude, String provider_name, String title, String first_name, String last_name, String other_name, int verified, String profile_image_url, int item_count) {
        this.id = id;
        this.cart_id = cart_id;
        this.order_id = order_id;
        this.provider_id = provider_id;
        this.customer_id = customer_id;
        this.status = status;
        this.shipping_fee = shipping_fee;
        this.longitude = longitude;
        this.latitude = latitude;
        this.provider_name = provider_name;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_name = other_name;
        this.verified = verified;
        this.profile_image_url = profile_image_url;
        this.item_count = item_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
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

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }
}
