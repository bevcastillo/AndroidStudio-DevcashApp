package com.example.devcash.Object;

import android.net.Uri;

import java.math.BigDecimal;
import java.util.Date;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class ProductObj {
    private Uri prod_imageUri;
    private int prod_id, disc_id, category_id, inventory_id, prod_qty_per_unit,
            prod_condition_count, prod_ROP, prod_exp_date, prod_exp_date_count;
    private String prod_qrcode, prod_title, prod_unit_measure, prod_status, prod_condition;
    private double prod_price;

    public ProductObj(Uri prod_image, int prod_id, int disc_id, int category_id, int inventory_id,
                      int prod_qty_per_unit, int prod_condition_count, int prod_ROP, int prod_exp_date,
                      int prod_exp_date_count, String prod_qrcode, String prod_title, String prod_unit_measure,
                      String prod_status, String prod_condition, double prod_price) {
        this.prod_imageUri = prod_image;
        this.prod_id = prod_id;
        this.disc_id = disc_id;
        this.category_id = category_id;
        this.inventory_id = inventory_id;
        this.prod_qty_per_unit = prod_qty_per_unit;
        this.prod_condition_count = prod_condition_count;
        this.prod_ROP = prod_ROP;
        this.prod_exp_date = prod_exp_date;
        this.prod_exp_date_count = prod_exp_date_count;
        this.prod_qrcode = prod_qrcode;
        this.prod_title = prod_title;
        this.prod_unit_measure = prod_unit_measure;
        this.prod_status = prod_status;
        this.prod_condition = prod_condition;
        this.prod_price = prod_price;
    }

    public Uri getProd_image() {
        return prod_imageUri;
    }

    public void setProd_image(Uri prod_image) {
        this.prod_imageUri = prod_image;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getDisc_id() {
        return disc_id;
    }

    public void setDisc_id(int disc_id) {
        this.disc_id = disc_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getProd_qty_per_unit() {
        return prod_qty_per_unit;
    }

    public void setProd_qty_per_unit(int prod_qty_per_unit) {
        this.prod_qty_per_unit = prod_qty_per_unit;
    }

    public int getProd_condition_count() {
        return prod_condition_count;
    }

    public void setProd_condition_count(int prod_condition_count) {
        this.prod_condition_count = prod_condition_count;
    }

    public int getProd_ROP() {
        return prod_ROP;
    }

    public void setProd_ROP(int prod_ROP) {
        this.prod_ROP = prod_ROP;
    }

    public int getProd_exp_date() {
        return prod_exp_date;
    }

    public void setProd_exp_date(int prod_exp_date) {
        this.prod_exp_date = prod_exp_date;
    }

    public int getProd_exp_date_count() {
        return prod_exp_date_count;
    }

    public void setProd_exp_date_count(int prod_exp_date_count) {
        this.prod_exp_date_count = prod_exp_date_count;
    }

    public String getProd_qrcode() {
        return prod_qrcode;
    }

    public void setProd_qrcode(String prod_qrcode) {
        this.prod_qrcode = prod_qrcode;
    }

    public String getProd_title() {
        return prod_title;
    }

    public void setProd_title(String prod_title) {
        this.prod_title = prod_title;
    }

    public String getProd_unit_measure() {
        return prod_unit_measure;
    }

    public void setProd_unit_measure(String prod_unit_measure) {
        this.prod_unit_measure = prod_unit_measure;
    }

    public String getProd_status() {
        return prod_status;
    }

    public void setProd_status(String prod_status) {
        this.prod_status = prod_status;
    }

    public String getProd_condition() {
        return prod_condition;
    }

    public void setProd_condition(String prod_condition) {
        this.prod_condition = prod_condition;
    }

    public double getProd_price() {
        return prod_price;
    }

    public void setProd_price(double prod_price) {
        this.prod_price = prod_price;
    }
}
