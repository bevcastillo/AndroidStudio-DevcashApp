package com.example.devcash.Object;
/*
    created by Beverly Castillo on August 1, 2019
 */

import android.net.Uri;

public class Services {
    String service_name, service_status;
    double service_price, service_subtotal, service_disc_price;
    int service_qty;
    public Discount discount;
    public Category category;
    public QRCode qrCode;

    public Services() {
    }

    public Services(String service_name, String service_status, double service_price, double discountedPrice) {
    }

//    public Services(String service_name, String service_status, double service_price) {
//        this.service_name = service_name;
//        this.service_status = service_status;
//        this.service_price = service_price;
//    }


    public Services(String service_name, String service_status, double service_price, int service_qty, double service_disc_price) {
        this.service_name = service_name;
        this.service_status = service_status;
        this.service_price = service_price;
        this.service_qty = service_qty;
        this.service_disc_price = service_disc_price;
    }



    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public double getService_price() {
        return service_price;
    }

    public void setService_price(double service_price) {
        this.service_price = service_price;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public int getService_qty() {
        return service_qty;
    }

    public void setService_qty(int service_qty) {
        this.service_qty = service_qty;
    }

    public QRCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }

    public double getService_subtotal() {
        return service_subtotal;
    }

    public void setService_subtotal(double service_subtotal) {
        this.service_subtotal = service_subtotal;
    }

    public double getService_disc_price() {
        return service_disc_price;
    }

    public void setService_disc_price(double service_disc_price) {
        this.service_disc_price = service_disc_price;
    }
}
