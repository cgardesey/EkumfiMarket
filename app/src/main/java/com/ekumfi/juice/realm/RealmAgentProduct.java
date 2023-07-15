package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmAgentProduct extends RealmObject {

    private int id;
    @PrimaryKey
    private String agent_product_id;
    private String product_id;
    private String agent_id;
    private int unit_quantity;
    private double unit_price;
    private int quantity_available;
    private String created_at;
    private String updated_at;

    private String product_name;
    private String product_image_url;
    private double longitude;
    private double latitude;
    private String title;
    private String first_name;
    private String last_name;
    private String other_names;
    private int verified;
    private String availability;
    private String profile_image_url;

    public RealmAgentProduct() {

    }

    public RealmAgentProduct(int id, String agent_product_id, String product_id, String agent_id, int unit_quantity, double unit_price, int quantity_available, String created_at, String updated_at) {
        this.id = id;
        this.agent_product_id = agent_product_id;
        this.product_id = product_id;
        this.agent_id = agent_id;
        this.unit_quantity = unit_quantity;
        this.unit_price = unit_price;
        this.quantity_available = quantity_available;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public RealmAgentProduct(int id, String agent_product_id, String product_id, String agent_id, int unit_quantity, double unit_price, int quantity_available, String created_at, String updated_at, String product_name, String product_image_url, double longitude, double latitude, String title, String first_name, String last_name, String other_names, int verified, String availability, String profile_image_url) {
        this.id = id;
        this.agent_product_id = agent_product_id;
        this.product_id = product_id;
        this.agent_id = agent_id;
        this.unit_quantity = unit_quantity;
        this.unit_price = unit_price;
        this.quantity_available = quantity_available;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.product_name = product_name;
        this.product_image_url = product_image_url;
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_names = other_names;
        this.verified = verified;
        this.availability = availability;
        this.profile_image_url = profile_image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgent_product_id() {
        return agent_product_id;
    }

    public void setAgent_product_id(String agent_product_id) {
        this.agent_product_id = agent_product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public int getUnit_quantity() {
        return unit_quantity;
    }

    public void setUnit_quantity(int unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
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

    public String getOther_names() {
        return other_names;
    }

    public void setOther_names(String other_names) {
        this.other_names = other_names;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }
}
