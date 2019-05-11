package com.example.devcash;

public class EnterpriseList {

    private String enterpriseTitle, enterpriseDetails;

    //constructor

    public EnterpriseList(String enterpriseTitle, String enterpriseDetails) {
        super();
        this.enterpriseTitle = enterpriseTitle;
        this.enterpriseDetails = enterpriseDetails;
    }

    //getters and setters

    public String getEnterpriseTitle() {
        return enterpriseTitle;
    }

    public void setEnterpriseTitle(String enterpriseTitle) {
        this.enterpriseTitle = enterpriseTitle;
    }

    public String getEnterpriseDetails() {
        return enterpriseDetails;
    }

    public void setEnterpriseDetails(String enterpriseDetails) {
        this.enterpriseDetails = enterpriseDetails;
    }
}
