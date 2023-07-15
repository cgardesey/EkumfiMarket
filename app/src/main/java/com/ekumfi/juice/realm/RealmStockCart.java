package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmStockCart extends RealmObject {

    private int id;
    @PrimaryKey
    private String stock_cart_id;
    private String order_id;
    private String agent_id;
    private String seller_id;
    private double shipping_fee;
    private int delivered;
    private int paid;
    private String created_at;
    private String updated_at;
    private String status;
    private double agent_longitude;
    private double agent_latitude;
    private String title;
    private String first_name;
    private String last_name;
    private String other_name;
    private int agent_verified;
    private String profile_image_url;
    private String agent_availability;
    private double seller_longitude;
    private double seller_latitude;
    private String shop_name;
    private int seller_verified;
    private String shop_image_url;
    private String seller_availability;

    private int item_count;

    public RealmStockCart() {

    }

    public RealmStockCart(int id, String stock_cart_id, String order_id, String agent_id, String seller_id, double shipping_fee, int delivered, int paid, String created_at, String updated_at, String status, double agent_longitude, double agent_latitude, String title, String first_name, String last_name, String other_name, int agent_verified, String profile_image_url, String agent_availability, double seller_longitude, double seller_latitude, String shop_name, int seller_verified, String shop_image_url, String seller_availability, int item_count) {
        this.id = id;
        this.stock_cart_id = stock_cart_id;
        this.order_id = order_id;
        this.agent_id = agent_id;
        this.seller_id = seller_id;
        this.shipping_fee = shipping_fee;
        this.delivered = delivered;
        this.paid = paid;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.agent_longitude = agent_longitude;
        this.agent_latitude = agent_latitude;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_name = other_name;
        this.agent_verified = agent_verified;
        this.profile_image_url = profile_image_url;
        this.agent_availability = agent_availability;
        this.seller_longitude = seller_longitude;
        this.seller_latitude = seller_latitude;
        this.shop_name = shop_name;
        this.seller_verified = seller_verified;
        this.shop_image_url = shop_image_url;
        this.seller_availability = seller_availability;
        this.item_count = item_count;
    }

    public RealmStockCart(int id, String stock_cart_id, String order_id, String agent_id, String seller_id, double shipping_fee, int delivered, int paid, String created_at, String updated_at, String status, double seller_longitude, double seller_latitude, String shop_name, int seller_verified, String shop_image_url, String seller_availability, int item_count) {
        this.id = id;
        this.stock_cart_id = stock_cart_id;
        this.order_id = order_id;
        this.agent_id = agent_id;
        this.seller_id = seller_id;
        this.shipping_fee = shipping_fee;
        this.delivered = delivered;
        this.paid = paid;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.seller_longitude = seller_longitude;
        this.seller_latitude = seller_latitude;
        this.shop_name = shop_name;
        this.seller_verified = seller_verified;
        this.shop_image_url = shop_image_url;
        this.seller_availability = seller_availability;
        this.item_count = item_count;
    }

    public RealmStockCart(int id, String stock_cart_id, String order_id, String agent_id, String seller_id, double shipping_fee, int delivered, int paid, String created_at, String updated_at, String status, double agent_longitude, double agent_latitude, String title, String first_name, String last_name, String other_name, int agent_verified, String profile_image_url, String agent_availability, int item_count) {
        this.id = id;
        this.stock_cart_id = stock_cart_id;
        this.order_id = order_id;
        this.agent_id = agent_id;
        this.seller_id = seller_id;
        this.shipping_fee = shipping_fee;
        this.delivered = delivered;
        this.paid = paid;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.agent_longitude = agent_longitude;
        this.agent_latitude = agent_latitude;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_name = other_name;
        this.agent_verified = agent_verified;
        this.profile_image_url = profile_image_url;
        this.agent_availability = agent_availability;
        this.item_count = item_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStock_cart_id() {
        return stock_cart_id;
    }

    public void setStock_cart_id(String stock_cart_id) {
        this.stock_cart_id = stock_cart_id;
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

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
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

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public double getAgent_longitude() {
        return agent_longitude;
    }

    public void setAgent_longitude(double agent_longitude) {
        this.agent_longitude = agent_longitude;
    }

    public double getAgent_latitude() {
        return agent_latitude;
    }

    public void setAgent_latitude(double agent_latitude) {
        this.agent_latitude = agent_latitude;
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

    public int getAgent_verified() {
        return agent_verified;
    }

    public void setAgent_verified(int agent_verified) {
        this.agent_verified = agent_verified;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getAgent_availability() {
        return agent_availability;
    }

    public void setAgent_availability(String agent_availability) {
        this.agent_availability = agent_availability;
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

    public int getSeller_verified() {
        return seller_verified;
    }

    public void setSeller_verified(int seller_verified) {
        this.seller_verified = seller_verified;
    }

    public String getShop_image_url() {
        return shop_image_url;
    }

    public void setShop_image_url(String shop_image_url) {
        this.shop_image_url = shop_image_url;
    }

    public String getSeller_availability() {
        return seller_availability;
    }

    public void setSeller_availability(String seller_availability) {
        this.seller_availability = seller_availability;
    }
}
