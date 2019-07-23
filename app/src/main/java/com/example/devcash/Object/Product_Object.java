package com.example.devcash.Object;

import android.net.Uri;

import java.math.BigDecimal;
import java.util.Date;

public class Product_Object {

    Uri prod_image;
    String prod_name, prod_condition, prod_status, prod, prod_qrcode;
    int prod_stock_count, prod_rop, prod_exp_date_count;
    Date prod_exp_date;
    BigDecimal prod_price, discount;

    //constructor

    public Product_Object(Uri prod_image, String prod_name, String prod_condition,
                          String prod_status, String prod, String prod_qrcode, int prod_stock_count,
                          int prod_rop, int prod_exp_date_count, Date prod_exp_date, BigDecimal prod_price,
                          BigDecimal discount) {
        this.prod_image = prod_image;
        this.prod_name = prod_name;
        this.prod_condition = prod_condition;
        this.prod_status = prod_status;
        this.prod = prod;
        this.prod_qrcode = prod_qrcode;
        this.prod_stock_count = prod_stock_count;
        this.prod_rop = prod_rop;
        this.prod_exp_date_count = prod_exp_date_count;
        this.prod_exp_date = prod_exp_date;
        this.prod_price = prod_price;
        this.discount = discount;
    }

    //getters and setters

    public Uri getProd_image() {
        return prod_image;
    }

    public void setProd_image(Uri prod_image) {
        this.prod_image = prod_image;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_condition() {
        return prod_condition;
    }

    public void setProd_condition(String prod_condition) {
        this.prod_condition = prod_condition;
    }

    public String getProd_status() {
        return prod_status;
    }

    public void setProd_status(String prod_status) {
        this.prod_status = prod_status;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getProd_qrcode() {
        return prod_qrcode;
    }

    public void setProd_qrcode(String prod_qrcode) {
        this.prod_qrcode = prod_qrcode;
    }

    public int getProd_stock_count() {
        return prod_stock_count;
    }

    public void setProd_stock_count(int prod_stock_count) {
        this.prod_stock_count = prod_stock_count;
    }

    public int getProd_rop() {
        return prod_rop;
    }

    public void setProd_rop(int prod_rop) {
        this.prod_rop = prod_rop;
    }

    public int getProd_exp_date_count() {
        return prod_exp_date_count;
    }

    public void setProd_exp_date_count(int prod_exp_date_count) {
        this.prod_exp_date_count = prod_exp_date_count;
    }

    public Date getProd_exp_date() {
        return prod_exp_date;
    }

    public void setProd_exp_date(Date prod_exp_date) {
        this.prod_exp_date = prod_exp_date;
    }

    public BigDecimal getProd_price() {
        return prod_price;
    }

    public void setProd_price(BigDecimal prod_price) {
        this.prod_price = prod_price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
