package com.example.devcash.Object;

public class Serviceslistdata {
    public String servname, servstatus, service_image, service_reference;
    public int servqty;
    double servprice, discounted_price;
    int click;
    Discount discount;
    Category category;

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


    public double getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(double discounted_price) {
        this.discounted_price = discounted_price;
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

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getService_reference() {
        return service_reference;
    }

    public void setService_reference(String service_reference) {
        this.service_reference = service_reference;
    }
}
