package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmService extends RealmObject {

    private int id;
    @PrimaryKey
    private String service_id;
    private String service_category;
    private String name;
    private String description;
    private double min_charge_amount;
    private double max_charge_amount;
    private String rating;
    private String total_rating;
    private String provider_id;
    private String created_at;
    private String updated_at;

    private double longitude;
    private double latitude;
    private String provider_name;
    private String title;
    private String first_name;
    private String last_name;
    private String other_name;
    private int verified;
    private String availability;
    private String url;

    public RealmService() {

    }

    public RealmService(int id, String service_id, String service_category, String name, String description, double min_charge_amount, double max_charge_amount, String rating, String total_rating, String provider_id, String created_at, String updated_at, double longitude, double latitude, String provider_name, String title, String first_name, String last_name, String other_name, int verified, String availability, String url) {
        this.id = id;
        this.service_id = service_id;
        this.service_category = service_category;
        this.name = name;
        this.description = description;
        this.min_charge_amount = min_charge_amount;
        this.max_charge_amount = max_charge_amount;
        this.rating = rating;
        this.total_rating = total_rating;
        this.provider_id = provider_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.longitude = longitude;
        this.latitude = latitude;
        this.provider_name = provider_name;
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

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_category() {
        return service_category;
    }

    public void setService_category(String service_category) {
        this.service_category = service_category;
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

    public double getMin_charge_amount() {
        return min_charge_amount;
    }

    public void setMin_charge_amount(double min_charge_amount) {
        this.min_charge_amount = min_charge_amount;
    }

    public double getMax_charge_amount() {
        return max_charge_amount;
    }

    public void setMax_charge_amount(double max_charge_amount) {
        this.max_charge_amount = max_charge_amount;
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
