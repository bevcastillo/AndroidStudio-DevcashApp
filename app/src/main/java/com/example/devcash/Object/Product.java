package com.example.devcash.Object;

public class Product {
    String prod_name, prod_unitof_measure, prod_status, prod_soldby;
    double prod_price, prod_rop;
    public Category category;

    public Product() {
    }

    public Product(String prod_name, String prod_unitof_measure, String prod_status, String prod_soldby, double prod_price, double prod_rop) {
        this.prod_name = prod_name;
        this.prod_unitof_measure = prod_unitof_measure;
        this.prod_status = prod_status;
        this.prod_soldby = prod_soldby;
        this.prod_price = prod_price;
        this.prod_rop = prod_rop;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_unitof_measure() {
        return prod_unitof_measure;
    }

    public void setProd_unitof_measure(String prod_unitof_measure) {
        this.prod_unitof_measure = prod_unitof_measure;
    }

    public String getProd_status() {
        return prod_status;
    }

    public void setProd_status(String prod_status) {
        this.prod_status = prod_status;
    }

    public double getProd_price() {
        return prod_price;
    }

    public void setProd_price(double prod_price) {
        this.prod_price = prod_price;
    }


    public double getProd_rop() {
        return prod_rop;
    }

    public void setProd_rop(double prod_rop) {
        this.prod_rop = prod_rop;
    }

    public String getProd_soldby() {
        return prod_soldby;
    }

    public void setProd_soldby(String prod_soldby) {
        this.prod_soldby = prod_soldby;
    }
}
