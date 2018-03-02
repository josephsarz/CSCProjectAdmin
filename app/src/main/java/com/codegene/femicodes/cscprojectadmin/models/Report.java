package com.codegene.femicodes.cscprojectadmin.models;

/**
 * Created by femicodes on 1/12/2018.
 */

public class Report {
    private String productName;
    private String productType;
    private String batchNumber;
    private String nafdacNumber;
    private String expiringDate;
    private String manufacturerName;
    private String storeLocation;
    private String complainDetails;
    private String complaintName;
    private String complaintPhoneNo;
    private String complaintEmail;
    private String state;
    private String lga;
    private String town;

    public Report() {
    }

    public Report(String productName, String productType, String storeLocation, String complainDetails, String complaintName, String complaintPhoneNo, String complaintEmail, String state, String lga, String town) {
        this.productName = productName;
        this.productType = productType;
        this.storeLocation = storeLocation;
        this.complainDetails = complainDetails;
        this.complaintName = complaintName;
        this.complaintPhoneNo = complaintPhoneNo;
        this.complaintEmail = complaintEmail;
        this.state = state;
        this.lga = lga;
        this.town = town;
    }

    public Report(String productName, String productType, String storeLocation, String complainDetails, String complaintName, String state, String lga, String town) {
        this.productName = productName;
        this.productType = productType;
        this.storeLocation = storeLocation;
        this.complainDetails = complainDetails;
        this.complaintName = complaintName;
        this.state = state;
        this.lga = lga;
        this.town = town;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getComplainDetails() {
        return complainDetails;
    }

    public void setComplainDetails(String complainDetails) {
        this.complainDetails = complainDetails;
    }

    public String getComplaintName() {
        return complaintName;
    }

    public void setComplaintName(String complaintName) {
        this.complaintName = complaintName;
    }

    public String getComplaintPhoneNo() {
        return complaintPhoneNo;
    }

    public void setComplaintPhoneNo(String complaintPhoneNo) {
        this.complaintPhoneNo = complaintPhoneNo;
    }

    public String getComplaintEmail() {
        return complaintEmail;
    }

    public void setComplaintEmail(String complaintEmail) {
        this.complaintEmail = complaintEmail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
