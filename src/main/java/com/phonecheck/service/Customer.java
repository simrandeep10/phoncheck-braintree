package com.phonecheck.service;

import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class Customer {

    private Calendar createdAt;
    private Calendar updatedAt;
    private String company;
    private String email;
    private String fax;
    private String firstName;
    private String id;
    private String lastName;
    private String phone;
    private String website;


    public Customer(NodeWrapper node) {
        id = node.findString("id");
        firstName = node.findString("first-name");
        lastName = node.findString("last-name");
        company = node.findString("company");
        email = node.findString("email");
        fax = node.findString("fax");
        phone = node.findString("phone");
        website = node.findString("website");
        createdAt = node.findDateTime("created-at");
        updatedAt = node.findDateTime("updated-at");

    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }


}
