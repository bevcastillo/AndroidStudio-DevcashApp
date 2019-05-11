package com.example.devcash;

public class NotificationsSettingsList {

    private String notificationTitle, notificationDetails;

    //constructor

    public NotificationsSettingsList(String notificationTitle, String notificationDetails) {
        super();
        this.notificationTitle = notificationTitle;
        this.notificationDetails = notificationDetails;
    }

    //setters and getters

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(String notificationDetails) {
        this.notificationDetails = notificationDetails;
    }
}
