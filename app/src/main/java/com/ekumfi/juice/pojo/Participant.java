package com.ekumfi.juice.pojo;

/**
 * Created by Nana on 12/16/2017.
 */

public class Participant {
    private String participantid;
    private String name;
    private String profileimgurl;
    private boolean teacher;
    private int connected;


    public Participant() {

    }

    public Participant(String participantid, String name, String profileimgurl, boolean teacher, int connected) {
        this.participantid = participantid;
        this.name = name;
        this.profileimgurl = profileimgurl;
        this.teacher = teacher;
        this.connected = connected;
    }

    public String getParticipantid() {
        return participantid;
    }

    public void setParticipantid(String participantid) {
        this.participantid = participantid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public int getConnected() {
        return connected;
    }

    public void setConnected(int connected) {
        this.connected = connected;
    }
}
