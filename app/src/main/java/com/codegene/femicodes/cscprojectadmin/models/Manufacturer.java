package com.codegene.femicodes.cscprojectadmin.models;

/**
 * Created by femicodes on 1/12/2018.
 */

public class Manufacturer {

    String id;
    String name;
    String address;
    String country;
    String email;
    String phone;
    String website;

    public Manufacturer() {
    }

    public Manufacturer(String name, String address, String country, String email, String phone, String website) {
        this.name = name;
        this.address = address;
        this.country = country;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
