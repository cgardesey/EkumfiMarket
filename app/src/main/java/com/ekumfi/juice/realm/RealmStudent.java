package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**z
 */

public class RealmStudent extends RealmObject {

    private int id;
    @PrimaryKey
    private String infoid;
    private String profilepicurl;
    private String title;
    private String firstname;
    private String lastname;
    private String othername;
    private String gender;
    private String emailaddress;
    private String guardianphonenumber;
    private String guardian2phonenumber;
    private String dob;
    private String homeaddress;
    private String maritalstatus;
    private String primarycontact;
    private String auxiliarycontact;
    private String highestedulevel;
    private String highesteduinstitutionname;
    private String created_at;
    private String updated_at;

    public RealmStudent() {

    }

    public RealmStudent(String infoid, String profilepicurl, String title, String firstname, String lastname, String othername, String gender, String emailaddress, String guardianphonenumber, String guardian2phonenumber, String dob, String homeaddress, String maritalstatus, String primarycontact, String auxiliarycontact, String highestedulevel, String highesteduinstitutionname, String created_at, String updated_at) {
        this.infoid = infoid;
        this.profilepicurl = profilepicurl;
        this.title = title;
        this.firstname = firstname;
        this.lastname = lastname;
        this.othername = othername;
        this.gender = gender;
        this.emailaddress = emailaddress;
        this.guardianphonenumber = guardianphonenumber;
        this.guardian2phonenumber = guardian2phonenumber;
        this.dob = dob;
        this.homeaddress = homeaddress;
        this.maritalstatus = maritalstatus;
        this.primarycontact = primarycontact;
        this.auxiliarycontact = auxiliarycontact;
        this.highestedulevel = highestedulevel;
        this.highesteduinstitutionname = highesteduinstitutionname;
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

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getGuardianphonenumber() {
        return guardianphonenumber;
    }

    public void setGuardianphonenumber(String guardianphonenumber) {
        this.guardianphonenumber = guardianphonenumber;
    }

    public String getGuardian2phonenumber() {
        return guardian2phonenumber;
    }

    public void setGuardian2phonenumber(String guardian2phonenumber) {
        this.guardian2phonenumber = guardian2phonenumber;
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

    public String getHighestedulevel() {
        return highestedulevel;
    }

    public void setHighestedulevel(String highestedulevel) {
        this.highestedulevel = highestedulevel;
    }

    public String getHighesteduinstitutionname() {
        return highesteduinstitutionname;
    }

    public void setHighesteduinstitutionname(String highesteduinstitutionname) {
        this.highesteduinstitutionname = highesteduinstitutionname;
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
