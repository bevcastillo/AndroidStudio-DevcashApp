package com.example.devcash;

import java.io.Serializable;

public class EnterpriseList {

    private String enterpriseTitle, enterpriseDetails;
    private int icon;

    //constructor

    public EnterpriseList(String enterpriseTitle, String enterpriseDetails, int icon) {
        super();
        this.enterpriseTitle = enterpriseTitle;
        this.enterpriseDetails = enterpriseDetails;
        this.icon = icon;
    }

    //setters and getters

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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
