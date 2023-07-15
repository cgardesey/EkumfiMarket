package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmInstitution extends RealmObject {

    private int id;
    @PrimaryKey
    private String institutionid;
    private String name;
    private String level;
    private String address;
    private String location;
    private String contact;
    private String website;
    private String logourl;
    private String dateregistered;
    private String userid;
    private int internalinstitution;
    private String currency;
    private String price_day;
    private String price_week;
    private String price;
    private int active;
    private String created_at;
    private String updated_at;

    public RealmInstitution() {

    }

    public RealmInstitution(String institutionid, String name, String level, String address, String location, String contact, String website, String logourl, String dateregistered, String userid, int internalinstitution, String currency, String price_day, String price_week, String price, int active, String created_at, String updated_at) {
        this.institutionid = institutionid;
        this.name = name;
        this.level = level;
        this.address = address;
        this.location = location;
        this.contact = contact;
        this.website = website;
        this.logourl = logourl;
        this.dateregistered = dateregistered;
        this.userid = userid;
        this.internalinstitution = internalinstitution;
        this.currency = currency;
        this.price_day = price_day;
        this.price_week = price_week;
        this.price = price;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(String institutionid) {
        this.institutionid = institutionid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(String dateregistered) {
        this.dateregistered = dateregistered;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getInternalinstitution() {
        return internalinstitution;
    }

    public void setInternalinstitution(int internalinstitution) {
        this.internalinstitution = internalinstitution;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice_day() {
        return price_day;
    }

    public void setPrice_day(String price_day) {
        this.price_day = price_day;
    }

    public String getPrice_week() {
        return price_week;
    }

    public void setPrice_week(String price_week) {
        this.price_week = price_week;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
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
