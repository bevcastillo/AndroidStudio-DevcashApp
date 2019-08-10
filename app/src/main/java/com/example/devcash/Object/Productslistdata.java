package com.example.devcash.Object;

public class Productslistdata {
    String prod_name, prod_unitof_measure, prod_status,prod_soldby, category_name, cond_name;
    double prod_price, prod_rop;
    int prod_stock;


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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getProd_stock() {
        return prod_stock;
    }

    public void setProd_stock(int prod_stock) {
        this.prod_stock = prod_stock;
    }

    public String getCond_name() {
        return cond_name;
    }

    public void setCond_name(String cond_name) {
        this.cond_name = cond_name;
    }
}
