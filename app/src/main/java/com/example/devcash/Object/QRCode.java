package com.example.devcash.Object;

public class QRCode {
    private String qr_category, qr_code;
    private double qr_price;

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
}