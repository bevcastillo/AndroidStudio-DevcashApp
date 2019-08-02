package com.example.devcash.Object;
/*
    created by Beverly Castillo on August 1, 2019
 */

import android.net.Uri;

public class Services {
    String service_name;
    double service_price;

    public Services() {
    }

    public Services(String service_name, double service_price) {
        this.service_name = service_name;
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
}
