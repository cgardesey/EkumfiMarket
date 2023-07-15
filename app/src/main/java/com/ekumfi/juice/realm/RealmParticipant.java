package com.ekumfi.juice.realm;

import io.realm.RealmObject;

public class RealmParticipant extends RealmObject {
  private   String std_name;
  private   String std_id;
  private   String std_image;
   private String phone_number;
  private   String login_time;
   private String ismute;
   private String participant_type;

    public RealmParticipant(String std_name, String std_id, String std_image, String phone_number) {
        this.std_name = std_name;
        this.std_id = std_id;
        this.std_image = std_image;
        this.phone_number = phone_number;
    }

    public RealmParticipant() {
    }

    public String getIsmute() {
        return ismute;
    }

    public void setIsmute(String ismute) {
        this.ismute = ismute;
    }

    public String getParticipant_type() {
        return participant_type;
    }

    public void setParticipant_type(String participant_type) {
        this.participant_type = participant_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public RealmParticipant(String std_name, String std_id, String std_image, String phone_number, String login_time, String ismute, String participant_type, String duration) {

        this.std_name = std_name;
        this.std_id = std_id;
        this.std_image = std_image;
        this.phone_number = phone_number;
        this.login_time = login_time;
        this.ismute = ismute;
        this.participant_type = participant_type;
        this.duration = duration;
    }

   private String duration;

    public RealmParticipant(String std_name, String std_id, String std_image, String phone_number, String login_time) {
        this.std_name = std_name;
        this.std_id = std_id;
        this.std_image = std_image;
        this.phone_number = phone_number;
        this.login_time = login_time;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStd_name() {
        return std_name;
    }

    public void setStd_name(String std_name) {
        this.std_name = std_name;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getStd_image() {
        return std_image;
    }

    public void setStd_image(String std_image) {
        this.std_image = std_image;
    }
}
