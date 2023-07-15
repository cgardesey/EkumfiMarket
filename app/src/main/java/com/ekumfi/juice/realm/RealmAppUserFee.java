package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmAppUserFee extends RealmObject {

    private int id;
    @PrimaryKey
    private String appuserfeeid;
    private String currency;
    private String priceperday;
    private String priceperweek;
    private String pricepermonth;
    private String created_at;
    private String updated_at;

    public RealmAppUserFee() {

    }

    public RealmAppUserFee(String appuserfeeid, String currency, String priceperday, String priceperweek, String pricepermonth, String created_at, String updated_at) {
        this.appuserfeeid = appuserfeeid;
        this.currency = currency;
        this.priceperday = priceperday;
        this.priceperweek = priceperweek;
        this.pricepermonth = pricepermonth;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppuserfeeid() {
        return appuserfeeid;
    }

    public void setAppuserfeeid(String appuserfeeid) {
        this.appuserfeeid = appuserfeeid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPriceperday() {
        return priceperday;
    }

    public void setPriceperday(String priceperday) {
        this.priceperday = priceperday;
    }

    public String getPriceperweek() {
        return priceperweek;
    }

    public void setPriceperweek(String priceperweek) {
        this.priceperweek = priceperweek;
    }

    public String getPricepermonth() {
        return pricepermonth;
    }

    public void setPricepermonth(String pricepermonth) {
        this.pricepermonth = pricepermonth;
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
