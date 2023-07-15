package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmDialcode extends RealmObject {

    @PrimaryKey
    private String dialcode;

    public RealmDialcode() {
    }

    public RealmDialcode(String dialcode) {
        this.dialcode = dialcode;
    }

    public String getDialcode() {
        return dialcode;
    }

    public void setDialcode(String dialcode) {
        this.dialcode = dialcode;
    }
}
