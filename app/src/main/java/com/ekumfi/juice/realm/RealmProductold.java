package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmProductold extends RealmObject {

    private int id;
    @PrimaryKey
    private String product_id;
    private String product_category;
    private String name;
    private String description;
    private int unit_quantity;
    private int quantity_available;
    private double unit_price;
    private String rating;
    private String total_rating;
    private String provider_id;
    private String provider_name;
    private String image;
    private String created_at;
    private String updated_at;

    private double longitude;
    private double latitude;
    private String title;
    private String first_name;
    private String last_name;
    private String other_name;
    private int verified;
    private String availability;
    private String url;

    public RealmProductold() {

    }

    public RealmProductold(int id, String product_id, String product_category, String name, String description, int unit_quantity, int quantity_available, double unit_price, String rating, String total_rating, String provider_id, String provider_name, String image, String created_at, String updated_at, double longitude, double latitude, String title, String first_name, String last_name, String other_name, int verified, String availability, String url) {
        this.id = id;
        this.product_id = product_id;
        this.product_category = product_category;
        this.name = name;
        this.description = description;
        this.unit_quantity = unit_quantity;
        this.quantity_available = quantity_available;
        this.unit_price = unit_price;
        this.rating = rating;
        this.total_rating = total_rating;
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_name = other_name;
        this.verified = verified;
        this.availability = availability;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
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

    public int getUnit_quantity() {
        return unit_quantity;
    }

    public void setUnit_quantity(int unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
