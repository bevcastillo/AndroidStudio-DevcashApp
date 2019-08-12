package com.example.devcash.Object;
/*
    created by Beverly Castillo on August 1, 2019
 */

import android.net.Uri;

public class Services {
    String service_name, service_status;
    double service_price;
    public Discount discount;
    public Category category;

    public Services() {
    }

    public Services(String service_name, String service_status, double service_price) {
        this.service_name = service_name;
        this.service_status = service_status;
        this.service_price = service_price;
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
}
