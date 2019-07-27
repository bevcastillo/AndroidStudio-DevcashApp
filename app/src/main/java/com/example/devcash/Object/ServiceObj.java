package com.example.devcash.Object;

import android.net.Uri;

/*
    created by Beverly Castillo on July 7, 2019
 */
public class ServiceObj {
    private int service_id, disc_id;
    private Uri service_imageUri;
    private String service_qrcode, service_title;
    private double service_price;

    public ServiceObj(int service_id, int disc_id, Uri service_imageUri, String service_qrcode,
                      String service_title, double service_price) {
        this.service_id = service_id;
        this.disc_id = disc_id;
        this.service_imageUri = service_imageUri;
        this.service_qrcode = service_qrcode;
        this.service_title = service_title;
        this.service_price = service_price;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getDisc_id() {
        return disc_id;
    }

    public void setDisc_id(int disc_id) {
        this.disc_id = disc_id;
    }

    public Uri getService_imageUri() {
        return service_imageUri;
    }

    public void setService_imageUri(Uri service_imageUri) {
        this.service_imageUri = service_imageUri;
    }

    public String getService_qrcode() {
        return service_qrcode;
    }

    public void setService_qrcode(String service_qrcode) {
        this.service_qrcode = service_qrcode;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public double getService_price() {
        return service_price;
    }

    public void setService_price(double service_price) {
        this.service_price = service_price;
    }
}
