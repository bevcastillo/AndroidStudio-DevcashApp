package com.example.devcash.Object;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class DiscountObj {
    private int disc_id, owner_id;
    private String disc_code, disc_status, disc_date_created, disc_start_date, disc_end_date;
    private double disc_value;

    public DiscountObj(int disc_id, int owner_id, String disc_code, String disc_status,
                       String disc_date_created, String disc_start_date, String disc_end_date,
                       double disc_value) {
        this.disc_id = disc_id;
        this.owner_id = owner_id;
        this.disc_code = disc_code;
        this.disc_status = disc_status;
        this.disc_date_created = disc_date_created;
        this.disc_start_date = disc_start_date;
        this.disc_end_date = disc_end_date;
        this.disc_value = disc_value;
    }

    public int getDisc_id() {
        return disc_id;
    }

    public void setDisc_id(int disc_id) {
        this.disc_id = disc_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getDisc_code() {
        return disc_code;
    }

    public void setDisc_code(String disc_code) {
        this.disc_code = disc_code;
    }

    public String getDisc_status() {
        return disc_status;
    }

    public void setDisc_status(String disc_status) {
        this.disc_status = disc_status;
    }

    public String getDisc_date_created() {
        return disc_date_created;
    }

    public void setDisc_date_created(String disc_date_created) {
        this.disc_date_created = disc_date_created;
    }

    public String getDisc_start_date() {
        return disc_start_date;
    }

    public void setDisc_start_date(String disc_start_date) {
        this.disc_start_date = disc_start_date;
    }

    public String getDisc_end_date() {
        return disc_end_date;
    }

    public void setDisc_end_date(String disc_end_date) {
        this.disc_end_date = disc_end_date;
    }

    public double getDisc_value() {
        return disc_value;
    }

    public void setDisc_value(double disc_value) {
        this.disc_value = disc_value;
    }
}
