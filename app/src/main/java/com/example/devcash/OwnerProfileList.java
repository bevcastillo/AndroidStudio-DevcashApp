package com.example.devcash;

public class OwnerProfileList {

    private String profileTitle, profileDetails;

    //constructor

    public OwnerProfileList(String profileTitle, String profileDetails) {
        super();
        this.profileTitle = profileTitle;
        this.profileDetails = profileDetails;
    }

    //getters and setters

    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }

    public String getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }
}
