package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class RealmDrawingCoordinate extends RealmObject {

    private int id;
    @PrimaryKey
    private String coordinatesid;
    private String classsessionid;
    private String instructorcourseid;
    private String x0;
    private String x1;
    private String y0;
    private String y1;
    private String color;
    private String strokeWidth;
    private String sessionId;
    private String clearpage;
    private String background;
    private int isstreaming;
    private int ispdf;
    private String pdfpath;
    private String doaction;
    private String coordinateTag;
    private String created_at;
    private String updated_at;

    public RealmDrawingCoordinate() {

    }

    public RealmDrawingCoordinate(String coordinatesid, String classsessionid, String instructorcourseid, String x0, String x1, String y0, String y1, String color, String strokeWidth, String sessionId, String clearpage, String background, int isstreaming, int ispdf, String pdfpath, String doaction, String coordinateTag, String created_at, String updated_at) {
        this.coordinatesid = coordinatesid;
        this.classsessionid = classsessionid;
        this.instructorcourseid = instructorcourseid;
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.sessionId = sessionId;
        this.clearpage = clearpage;
        this.background = background;
        this.isstreaming = isstreaming;
        this.ispdf = ispdf;
        this.pdfpath = pdfpath;
        this.doaction = doaction;
        this.coordinateTag = coordinateTag;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoordinatesid() {
        return coordinatesid;
    }

    public void setCoordinatesid(String coordinatesid) {
        this.coordinatesid = coordinatesid;
    }

    public String getClasssessionid() {
        return classsessionid;
    }

    public void setClasssessionid(String classsessionid) {
        this.classsessionid = classsessionid;
    }

    public String getInstructorcourseid() {
        return instructorcourseid;
    }

    public void setInstructorcourseid(String instructorcourseid) {
        this.instructorcourseid = instructorcourseid;
    }

    public String getX0() {
        return x0;
    }

    public void setX0(String x0) {
        this.x0 = x0;
    }

    public String getX1() {
        return x1;
    }

    public void setX1(String x1) {
        this.x1 = x1;
    }

    public String getY0() {
        return y0;
    }

    public void setY0(String y0) {
        this.y0 = y0;
    }

    public String getY1() {
        return y1;
    }

    public void setY1(String y1) {
        this.y1 = y1;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(String strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClearpage() {
        return clearpage;
    }

    public void setClearpage(String clearpage) {
        this.clearpage = clearpage;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getIsstreaming() {
        return isstreaming;
    }

    public void setIsstreaming(int isstreaming) {
        this.isstreaming = isstreaming;
    }

    public int getIspdf() {
        return ispdf;
    }

    public void setIspdf(int ispdf) {
        this.ispdf = ispdf;
    }

    public String getPdfpath() {
        return pdfpath;
    }

    public void setPdfpath(String pdfpath) {
        this.pdfpath = pdfpath;
    }

    public String getDoaction() {
        return doaction;
    }

    public void setDoaction(String doaction) {
        this.doaction = doaction;
    }

    public String getCoordinateTag() {
        return coordinateTag;
    }

    public void setCoordinateTag(String coordinateTag) {
        this.coordinateTag = coordinateTag;
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
