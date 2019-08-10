package com.example.devcash.Object;

public class ProductExpDate {
    String prod_expdate;
    int prod_expdatecount;

    public ProductExpDate() {
    }

//    public ProductExpDate(String prod_expdate, int prod_expdatecount) {
//        this.prod_expdate = prod_expdate;
//        this.prod_expdatecount = prod_expdatecount;
//    }

    public String getProd_expdate() {
        return prod_expdate;
    }

    public void setProd_expdate(String prod_expdate) {
        this.prod_expdate = prod_expdate;
    }

    public int getProd_expdatecount() {
        return prod_expdatecount;
    }

    public void setProd_expdatecount(int prod_expdatecount) {
        this.prod_expdatecount = prod_expdatecount;
    }
}
