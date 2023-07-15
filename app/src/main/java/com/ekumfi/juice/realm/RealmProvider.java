package com.ekumfi.juice.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmProvider extends RealmObject {

    private int id;
    @PrimaryKey
    private String provider_id;
    private String confirmation_token;

    private String title;
    private String first_name;
    private String last_name;
    private String other_name;
    private String gender;
    private String dob;
    private String marital_status;
    private String highest_edu_level;
    private String institution;
    private String about;


    private String provider_name;


    private String profile_image_url;
    private String primary_contact;
    private String auxiliary_contact;
    private String momo_number;
    private double longitude;
    private double latitude;
    private String digital_address;
    private String street_address;
    private String years_of_operation;
    private String date_registered;
    private int verified;

    private String category;
    private String identification_type;
    private String association_identification_type;
    private String identification_number;
    private String association_identification_number;
    private String identification_image_url;
    private String association_identification_image_url;

    private String vehicle_type;
    private String vehicle_registration_number;
    private String drivers_licence_image_url;
    private String drivers_licence_reverse_image_url;
    private String road_worthy_sticker_image_url;
    private String insurance_sticker_image_url;

    private String tin_number;
    private String availability;
    private String user_id;
    private String created_at;
    private String updated_at;

    private RealmUser user;
    RealmList<RealmProductold> products;
    RealmList<RealmService> services;

    public RealmProvider() {

    }

    public RealmProvider(int id, String provider_id, String confirmation_token, String title, String first_name, String last_name, String other_name, String gender, String dob, String marital_status, String highest_edu_level, String institution, String about, String provider_name, String profile_image_url, String primary_contact, String auxiliary_contact, String momo_number, double longitude, double latitude, String digital_address, String street_address, String years_of_operation, String date_registered, int verified, String category, String identification_type, String association_identification_type, String identification_number, String association_identification_number, String identification_image_url, String association_identification_image_url, String vehicle_type, String vehicle_registration_number, String drivers_licence_image_url, String drivers_licence_reverse_image_url, String road_worthy_sticker_image_url, String insurance_sticker_image_url, String tin_number, String availability, String user_id, String created_at, String updated_at) {
        this.id = id;
        this.provider_id = provider_id;
        this.confirmation_token = confirmation_token;
        this.title = title;
        this.first_name = first_name;
        this.last_name = last_name;
        this.other_name = other_name;
        this.gender = gender;
        this.dob = dob;
        this.marital_status = marital_status;
        this.highest_edu_level = highest_edu_level;
        this.institution = institution;
        this.about = about;
        this.provider_name = provider_name;
        this.profile_image_url = profile_image_url;
        this.primary_contact = primary_contact;
        this.auxiliary_contact = auxiliary_contact;
        this.momo_number = momo_number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.digital_address = digital_address;
        this.street_address = street_address;
        this.years_of_operation = years_of_operation;
        this.date_registered = date_registered;
        this.verified = verified;
        this.category = category;
        this.identification_type = identification_type;
        this.association_identification_type = association_identification_type;
        this.identification_number = identification_number;
        this.association_identification_number = association_identification_number;
        this.identification_image_url = identification_image_url;
        this.association_identification_image_url = association_identification_image_url;
        this.vehicle_type = vehicle_type;
        this.vehicle_registration_number = vehicle_registration_number;
        this.drivers_licence_image_url = drivers_licence_image_url;
        this.drivers_licence_reverse_image_url = drivers_licence_reverse_image_url;
        this.road_worthy_sticker_image_url = road_worthy_sticker_image_url;
        this.insurance_sticker_image_url = insurance_sticker_image_url;
        this.tin_number = tin_number;
        this.availability = availability;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getConfirmation_token() {
        return confirmation_token;
    }

    public void setConfirmation_token(String confirmation_token) {
        this.confirmation_token = confirmation_token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
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

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getHighest_edu_level() {
        return highest_edu_level;
    }

    public void setHighest_edu_level(String highest_edu_level) {
        this.highest_edu_level = highest_edu_level;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(String primary_contact) {
        this.primary_contact = primary_contact;
    }

    public String getAuxiliary_contact() {
        return auxiliary_contact;
    }

    public void setAuxiliary_contact(String auxiliary_contact) {
        this.auxiliary_contact = auxiliary_contact;
    }

    public String getMomo_number() {
        return momo_number;
    }

    public void setMomo_number(String momo_number) {
        this.momo_number = momo_number;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDigital_address() {
        return digital_address;
    }

    public void setDigital_address(String digital_address) {
        this.digital_address = digital_address;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getYears_of_operation() {
        return years_of_operation;
    }

    public void setYears_of_operation(String years_of_operation) {
        this.years_of_operation = years_of_operation;
    }

    public String getDate_registered() {
        return date_registered;
    }

    public void setDate_registered(String date_registered) {
        this.date_registered = date_registered;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdentification_type() {
        return identification_type;
    }

    public void setIdentification_type(String identification_type) {
        this.identification_type = identification_type;
    }

    public String getAssociation_identification_type() {
        return association_identification_type;
    }

    public void setAssociation_identification_type(String association_identification_type) {
        this.association_identification_type = association_identification_type;
    }

    public String getIdentification_number() {
        return identification_number;
    }

    public void setIdentification_number(String identification_number) {
        this.identification_number = identification_number;
    }

    public String getAssociation_identification_number() {
        return association_identification_number;
    }

    public void setAssociation_identification_number(String association_identification_number) {
        this.association_identification_number = association_identification_number;
    }

    public String getIdentification_image_url() {
        return identification_image_url;
    }

    public void setIdentification_image_url(String identification_image_url) {
        this.identification_image_url = identification_image_url;
    }

    public String getAssociation_identification_image_url() {
        return association_identification_image_url;
    }

    public void setAssociation_identification_image_url(String association_identification_image_url) {
        this.association_identification_image_url = association_identification_image_url;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_registration_number() {
        return vehicle_registration_number;
    }

    public void setVehicle_registration_number(String vehicle_registration_number) {
        this.vehicle_registration_number = vehicle_registration_number;
    }

    public String getDrivers_licence_image_url() {
        return drivers_licence_image_url;
    }

    public void setDrivers_licence_image_url(String drivers_licence_image_url) {
        this.drivers_licence_image_url = drivers_licence_image_url;
    }

    public String getDrivers_licence_reverse_image_url() {
        return drivers_licence_reverse_image_url;
    }

    public void setDrivers_licence_reverse_image_url(String drivers_licence_reverse_image_url) {
        this.drivers_licence_reverse_image_url = drivers_licence_reverse_image_url;
    }

    public String getRoad_worthy_sticker_image_url() {
        return road_worthy_sticker_image_url;
    }

    public void setRoad_worthy_sticker_image_url(String road_worthy_sticker_image_url) {
        this.road_worthy_sticker_image_url = road_worthy_sticker_image_url;
    }

    public String getInsurance_sticker_image_url() {
        return insurance_sticker_image_url;
    }

    public void setInsurance_sticker_image_url(String insurance_sticker_image_url) {
        this.insurance_sticker_image_url = insurance_sticker_image_url;
    }

    public String getTin_number() {
        return tin_number;
    }

    public void setTin_number(String tin_number) {
        this.tin_number = tin_number;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public RealmUser getUser() {
        return user;
    }

    public void setUser(RealmUser user) {
        this.user = user;
    }

    public RealmList<RealmProductold> getProducts() {
        return products;
    }

    public void setProducts(RealmList<RealmProductold> products) {
        this.products = products;
    }

    public RealmList<RealmService> getServices() {
        return services;
    }

    public void setServices(RealmList<RealmService> services) {
        this.services = services;
    }
}
