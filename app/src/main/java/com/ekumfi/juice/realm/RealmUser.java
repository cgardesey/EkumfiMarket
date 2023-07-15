package com.ekumfi.juice.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmUser extends RealmObject {

    private int id;
    @PrimaryKey
    private String user_id;
    private String phone_number;
    private String email;
    private String confirmation_token;
    private String role;
    private int connected;
    private String created_at;
    private String updated_at;

    private RealmCustomer2 customer;
    RealmList<RealmProvider> providers;

    public RealmUser() {

    }

    public RealmUser(String userid, String phone_number, String email, String confirmation_token, String role, int connected, String created_at, String updated_at, RealmCustomer2 customer, RealmList<RealmProvider> providers) {
        this.user_id = userid;
        this.phone_number = phone_number;
        this.email = email;
        this.confirmation_token = confirmation_token;
        this.role = role;
        this.connected = connected;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.customer = customer;
        this.providers = providers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getConnected() {
        return connected;
    }

    public void setConnected(int connected) {
        this.connected = connected;
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

    public RealmCustomer2 getCustomer() {
        return customer;
    }

    public void setCustomer(RealmCustomer2 customer) {
        this.customer = customer;
    }

    public RealmList<RealmProvider> getProviders() {
        return providers;
    }

    public void setProviders(RealmList<RealmProvider> providers) {
        this.providers = providers;
    }
}
