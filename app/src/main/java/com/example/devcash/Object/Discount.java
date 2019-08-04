package com.example.devcash.Object;

public class Discount {
    private String disc_code, disc_type, disc_start, disc_end, disc_status;
    private double disc_value;

    public Discount() {
    }

    public Discount(String disc_code, String disc_type, String disc_start, String disc_end, String disc_status, double disc_value) {
        this.disc_code = disc_code;
        this.disc_type = disc_type;
        this.disc_start = disc_start;
        this.disc_end = disc_end;
        this.disc_status = disc_status;
        this.disc_value = disc_value;
    }

    public String getDisc_code() {
        return disc_code;
    }

    public void setDisc_code(String disc_code) {
        this.disc_code = disc_code;
    }

    public String getDisc_type() {
        return disc_type;
    }

    public void setDisc_type(String disc_type) {
        this.disc_type = disc_type;
    }

    public String getDisc_start() {
        return disc_start;
    }

    public void setDisc_start(String disc_start) {
        this.disc_start = disc_start;
    }

    public String getDisc_end() {
        return disc_end;
    }

    public void setDisc_end(String disc_end) {
        this.disc_end = disc_end;
    }

    public String getDisc_status() {
        return disc_status;
    }

    public void setDisc_status(String disc_status) {
        this.disc_status = disc_status;
    }

    public double getDisc_value() {
        return disc_value;
    }

    public void setDisc_value(double disc_value) {
        this.disc_value = disc_value;
    }
}
