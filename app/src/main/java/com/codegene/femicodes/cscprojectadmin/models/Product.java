package com.codegene.femicodes.cscprojectadmin.models;

/**
 * Created by femicodes on 1/11/2018.
 */

public class Product {

    String productId;
    String productName;
    String productType;
    String nafdacNumber;
    String imageUrl;
    String manufacturerName;
    String manufacturerId;
    String batchNumber;
    String manufacturingDate;
    String expiringDate;


    public Product() {
    }

    public Product(String productId, String productName, String productType, String nafdacNumber, String imageUrl, String manufacturerName, String batchNumber, String manufacturingDate, String expiringDate) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.nafdacNumber = nafdacNumber;
        this.imageUrl = imageUrl;
        this.manufacturerName = manufacturerName;
        this.batchNumber = batchNumber;
        this.manufacturingDate = manufacturingDate;
        this.expiringDate = expiringDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getNafdacNumber() {
        return nafdacNumber;
    }

    public void setNafdacNumber(String nafdacNumber) {
        this.nafdacNumber = nafdacNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(String expiringDate) {
        this.expiringDate = expiringDate;
    }
}