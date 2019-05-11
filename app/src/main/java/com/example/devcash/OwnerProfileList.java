package com.example.devcash;

public class OwnerProfileList {

    private String profileTitle, profileDetails;
    private int profileIcon;

    //constructor

    public OwnerProfileList(String profileTitle, String profileDetails, int profileIcon) {
        super();
        this.profileTitle = profileTitle;
        this.profileDetails = profileDetails;
        this.profileIcon = profileIcon;
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

    public int getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(int profileIcon) {
        this.profileIcon = profileIcon;
    }
}
