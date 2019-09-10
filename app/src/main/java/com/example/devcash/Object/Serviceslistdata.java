package com.example.devcash.Object;

public class Serviceslistdata {
    public String servname, servstatus;
    public int servqty;
    double servprice, service_disc_price;
    int click;

    public String getServname() {
        return servname;
    }

    public void setServname(String servname) {
        this.servname = servname;
    }

    public double getServprice() {
        return servprice;
    }

    public void setServprice(double servprice) {
        this.servprice = servprice;
    }

    public String getServstatus() {
        return servstatus;
    }

    public void setServstatus(String servstatus) {
        this.servstatus = servstatus;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getServqty() {
        return servqty;
    }

    public void setServqty(int servqty) {
        this.servqty = servqty;
    }

    public double getService_disc_price() {
        return service_disc_price;
    }

    public void setService_disc_price(double service_disc_price) {
        this.service_disc_price = service_disc_price;
    }
}
