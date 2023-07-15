package com.ekumfi.juice.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmPayment extends RealmObject {

    private int id;
    @PrimaryKey
    private String payment_id;
    private String msisdn;
    private String country_code;
    private String network;
    private String currency;
    private String amount;
    private String description;
    private String payment_ref;
    private String message;
    private String response_message;
    private String status;
    private String external_reference_no;
    private String transaction_status_reason;
    private String cart_id;
    private String stock_cart_id;
    private String order_id;


    private String duration;
    private String appuserdescription;
    private String appuserfeeexpirydate;
    private String expirydate;
    private String institutionfeeexpirydate;
    private String payerid;
    private String enrolmentid;
    private String institutionid;
    private String feetype;
    private boolean expired;
    private boolean appuserfeeexpired;
    private boolean institutionfeeexpired;
    private String created_at;
    private String updated_at;


    private String coursepath;
    private String institutionname;

    public RealmPayment() {

    }

    public RealmPayment(String payment_id, String msisdn, String country_code, String network, String currency, String amount, String appuserdescription, String description, String duration, String payment_ref, String external_reference_no, String message, String status, String transactionstatusreaso, String appuserfeeexpirydate, String expirydate, String institutionfeeexpirydate, String payerid, String enrolmentid, String institutionid, String feetype, boolean expired, boolean appuserfeeexpired, boolean institutionfeeexpired, String created_at, String updated_at) {
        this.payment_id = payment_id;
        this.msisdn = msisdn;
        this.country_code = country_code;
        this.network = network;
        this.currency = currency;
        this.amount = amount;
        this.appuserdescription = appuserdescription;
        this.description = description;
        this.duration = duration;
        this.payment_ref = payment_ref;
        this.external_reference_no = external_reference_no;
        this.message = message;
        this.status = status;
        this.transaction_status_reason = transactionstatusreaso;
        this.appuserfeeexpirydate = appuserfeeexpirydate;
        this.expirydate = expirydate;
        this.institutionfeeexpirydate = institutionfeeexpirydate;
        this.payerid = payerid;
        this.enrolmentid = enrolmentid;
        this.institutionid = institutionid;
        this.feetype = feetype;
        this.expired = expired;
        this.appuserfeeexpired = appuserfeeexpired;
        this.institutionfeeexpired = institutionfeeexpired;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public RealmPayment(int id, String payment_id, String msisdn, String country_code, String network, String currency, String amount, String description, String payment_ref, String message, String response_message, String status, String external_reference_no, String transaction_status_reason, String cart_id, String stock_cart_id, String order_id, String created_at, String updated_at) {
        this.id = id;
        this.payment_id = payment_id;
        this.msisdn = msisdn;
        this.country_code = country_code;
        this.network = network;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.payment_ref = payment_ref;
        this.message = message;
        this.response_message = response_message;
        this.status = status;
        this.external_reference_no = external_reference_no;
        this.transaction_status_reason = transaction_status_reason;
        this.cart_id = cart_id;
        this.stock_cart_id = stock_cart_id;
        this.order_id = order_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public void setPayment_ref(String payment_ref) {
        this.payment_ref = payment_ref;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternal_reference_no() {
        return external_reference_no;
    }

    public void setExternal_reference_no(String external_reference_no) {
        this.external_reference_no = external_reference_no;
    }

    public String getTransaction_status_reason() {
        return transaction_status_reason;
    }

    public void setTransaction_status_reason(String transaction_status_reason) {
        this.transaction_status_reason = transaction_status_reason;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAppuserdescription() {
        return appuserdescription;
    }

    public void setAppuserdescription(String appuserdescription) {
        this.appuserdescription = appuserdescription;
    }

    public String getAppuserfeeexpirydate() {
        return appuserfeeexpirydate;
    }

    public void setAppuserfeeexpirydate(String appuserfeeexpirydate) {
        this.appuserfeeexpirydate = appuserfeeexpirydate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getInstitutionfeeexpirydate() {
        return institutionfeeexpirydate;
    }

    public void setInstitutionfeeexpirydate(String institutionfeeexpirydate) {
        this.institutionfeeexpirydate = institutionfeeexpirydate;
    }

    public String getPayerid() {
        return payerid;
    }

    public void setPayerid(String payerid) {
        this.payerid = payerid;
    }

    public String getEnrolmentid() {
        return enrolmentid;
    }

    public void setEnrolmentid(String enrolmentid) {
        this.enrolmentid = enrolmentid;
    }

    public String getInstitutionid() {
        return institutionid;
    }

    public void setInstitutionid(String institutionid) {
        this.institutionid = institutionid;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isAppuserfeeexpired() {
        return appuserfeeexpired;
    }

    public void setAppuserfeeexpired(boolean appuserfeeexpired) {
        this.appuserfeeexpired = appuserfeeexpired;
    }

    public boolean isInstitutionfeeexpired() {
        return institutionfeeexpired;
    }

    public void setInstitutionfeeexpired(boolean institutionfeeexpired) {
        this.institutionfeeexpired = institutionfeeexpired;
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

    public String getCoursepath() {
        return coursepath;
    }

    public void setCoursepath(String coursepath) {
        this.coursepath = coursepath;
    }

    public String getInstitutionname() {
        return institutionname;
    }

    public void setInstitutionname(String institutionname) {
        this.institutionname = institutionname;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStock_cart_id() {
        return stock_cart_id;
    }

    public void setStock_cart_id(String stock_cart_id) {
        this.stock_cart_id = stock_cart_id;
    }
}
