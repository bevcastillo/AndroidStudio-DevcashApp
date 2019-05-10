package com.example.devcash;

public class SettingsList {

    private int settingsicon;
    private String settingstitle;

    //constructor
    public SettingsList(int settingsicon, String settingstitle) {
        super();
        this.settingsicon = settingsicon;
        this.settingstitle = settingstitle;
    }

    //getters and setters

    public int getSettingsicon() {
        return settingsicon;
    }

    public void setSettingsicon(int settingsicon) {
        this.settingsicon = settingsicon;
    }

    public String getSettingstitle() {
        return settingstitle;
    }

    public void setSettingstitle(String settingstitle) {
        this.settingstitle = settingstitle;
    }
}
