package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmCart extends RealmObject {

    private int id;
    @PrimaryKey
    private String cart_id;
    private String order_id;
    private String seller_id;
    private String consumer_id;
    private int delivered;
    private String created_at;
    private String updated_at;

    private String status;
    private double shipping_fee;

    private double seller_longitude;
    private double seller_latitude;
    private String shop_name;
    private int verified;
    private String shop_image_url;
    private double consumer_longitude;
    private double consumer_latitude;
    private String consumer_name;
    private String consumer_profile_image_url;
    private String availability;
    private int item_count;

    public RealmCart() {

    }

    public RealmCart(int id, String cart_id, String order_id, String seller_id, String consumer_id, int delivered, String created_at, String updated_at, String status, double shipping_fee, double seller_longitude, double seller_latitude, String shop_name, int verified, String shop_image_url, int item_count) {
        this.id = id;
        this.cart_id = cart_id;
        this.order_id = order_id;
        this.seller_id = seller_id;
        this.consumer_id = consumer_id;
        this.delivered = delivered;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.shipping_fee = shipping_fee;
        this.seller_longitude = seller_longitude;
        this.seller_latitude = seller_latitude;
        this.shop_name = shop_name;
        this.verified = verified;
        this.shop_image_url = shop_image_url;
        this.item_count = item_count;
    }

    public RealmCart(int id, String cart_id, String order_id, String seller_id, String consumer_id, int delivered, String created_at, String updated_at, String status, double shipping_fee, double consumer_longitude, double consumer_latitude, String consumer_name, String consumer_profile_image_url, String availability, int item_count) {
        this.id = id;
        this.cart_id = cart_id;
        this.order_id = order_id;
        this.seller_id = seller_id;
        this.consumer_id = consumer_id;
        this.delivered = delivered;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.shipping_fee = shipping_fee;
        this.consumer_longitude = consumer_longitude;
        this.consumer_latitude = consumer_latitude;
        this.consumer_name = consumer_name;
        this.consumer_profile_image_url = consumer_profile_image_url;
        this.availability = availability;
        this.item_count = item_count;
    }

    public RealmCart(int id, String cart_id, String order_id, String seller_id, String consumer_id, int delivered, String created_at, String updated_at, String status, double shipping_fee, double seller_longitude, double seller_latitude, String shop_name, int verified, String shop_image_url, double consumer_longitude, double consumer_latitude, String consumer_name, String consumer_profile_image_url, String availability, int item_count) {
        this.id = id;
        this.cart_id = cart_id;
        this.order_id = order_id;
        this.seller_id = seller_id;
        this.consumer_id = consumer_id;
        this.delivered = delivered;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.shipping_fee = shipping_fee;
        this.seller_longitude = seller_longitude;
        this.seller_latitude = seller_latitude;
        this.shop_name = shop_name;
        this.verified = verified;
        this.shop_image_url = shop_image_url;
        this.consumer_longitude = consumer_longitude;
        this.consumer_latitude = consumer_latitude;
        this.consumer_name = consumer_name;
        this.consumer_profile_image_url = consumer_profile_image_url;
        this.availability = availability;
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

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
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

    public double getSeller_longitude() {
        return seller_longitude;
    }

    public void setSeller_longitude(double seller_longitude) {
        this.seller_longitude = seller_longitude;
    }

    public double getSeller_latitude() {
        return seller_latitude;
    }

    public void setSeller_latitude(double seller_latitude) {
        this.seller_latitude = seller_latitude;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getShop_image_url() {
        return shop_image_url;
    }

    public void setShop_image_url(String shop_image_url) {
        this.shop_image_url = shop_image_url;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public double getConsumer_longitude() {
        return consumer_longitude;
    }

    public void setConsumer_longitude(double consumer_longitude) {
        this.consumer_longitude = consumer_longitude;
    }

    public double getConsumer_latitude() {
        return consumer_latitude;
    }

    public void setConsumer_latitude(double consumer_latitude) {
        this.consumer_latitude = consumer_latitude;
    }

    public String getConsumer_name() {
        return consumer_name;
    }

    public void setConsumer_name(String consumer_name) {
        this.consumer_name = consumer_name;
    }

    public String getConsumer_profile_image_url() {
        return consumer_profile_image_url;
    }

    public void setConsumer_profile_image_url(String consumer_profile_image_url) {
        this.consumer_profile_image_url = consumer_profile_image_url;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
