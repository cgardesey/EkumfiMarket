package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmInstructor extends RealmObject {

    private int id;
    @PrimaryKey
    private String infoid;
    private String confirmation_token;
    private String profilepicurl;
    private String title;
    private String firstname;
    private String lastname;
    private String othername;
    private String gender;
    private String dob;
    private String homeaddress;
    private String maritalstatus;
    private String primarycontact;
    private String auxiliarycontact;
    private String edubackground;
    private String about;
    private String created_at;
    private String updated_at;

    public RealmInstructor() {

    }

    public RealmInstructor(String infoid, String confirmation_token, String profilepicurl, String title, String firstname, String lastname, String othername, String gender, String dob, String homeaddress, String maritalstatus, String primarycontact, String auxiliarycontact, String edubackground, String about, String created_at, String updated_at) {
        this.infoid = infoid;
        this.confirmation_token = confirmation_token;
        this.profilepicurl = profilepicurl;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.gender = gender;
        this.dob = dob;
        this.homeaddress = homeaddress;
        this.maritalstatus = maritalstatus;
        this.primarycontact = primarycontact;
        this.auxiliarycontact = auxiliarycontact;
        this.edubackground = edubackground;
        this.about = about;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHomeaddress() {
        return homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getPrimarycontact() {
        return primarycontact;
    }

    public void setPrimarycontact(String primarycontact) {
        this.primarycontact = primarycontact;
    }

    public String getAuxiliarycontact() {
        return auxiliarycontact;
    }

    public void setAuxiliarycontact(String auxiliarycontact) {
        this.auxiliarycontact = auxiliarycontact;
    }

    public String getEdubackground() {
        return edubackground;
    }

    public void setEdubackground(String edubackground) {
        this.edubackground = edubackground;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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
