package com.example.devcash.Object;

public class QRCode {
    private String qr_category, qr_code, qr_expdate, qr_reference;
    private double qr_price, qr_disc_price;

    public QRCode() {
    }

    public String getQr_category() {
        return qr_category;
    }

    public void setQr_category(String qr_category) {
        this.qr_category = qr_category;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public double getQr_price() {
        return qr_price;
    }

    public void setQr_price(double qr_price) {
        this.qr_price = qr_price;
    }

    public String getQr_expdate() {
        return qr_expdate;
    }

    public void setQr_expdate(String qr_expdate) {
        this.qr_expdate = qr_expdate;
    }

    public String getQr_reference() {
        return qr_reference;
    }

    public void setQr_reference(String qr_reference) {
        this.qr_reference = qr_reference;
    }

    public double getQr_disc_price() {
        return qr_disc_price;
    }

    public void setQr_disc_price(double qr_disc_price) {
        this.qr_disc_price = qr_disc_price;
    }
}
