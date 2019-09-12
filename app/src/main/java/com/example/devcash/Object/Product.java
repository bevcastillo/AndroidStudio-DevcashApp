package com.example.devcash.Object;

public class Product {
    String prod_name, prod_brand, prod_unitof_measure, prod_status, prod_datetimeadded, prod_expdate, prod_reference;
    double prod_price, prod_rop, prod_subtotal, prod_qty, prod_disc_price;
    double discounted_price;
    public Category category;
    int prod_stock, prod_expdatecount;
    public ProductCondition productCondition;
    public Discount discount;
    public QRCode qrCode;
    public ProductExpiration productExpiration;

    public Product() {
    }

//    public Product(String prod_name, String prod_unitof_measure, String prod_status, String prod_soldby, double prod_price, double prod_rop) {
//        this.prod_name = prod_name;
//        this.prod_unitof_measure = prod_unitof_measure;
//        this.prod_status = prod_status;
//        this.prod_soldby = prod_soldby;
//        this.prod_price = prod_price;
//        this.prod_rop = prod_rop;
//    }


//    public Product(String prod_name, String prod_unitof_measure, String prod_status, String prod_soldby, String prod_datetimeadded, double prod_price, double prod_rop, int prod_stock) {
//        this.prod_name = prod_name;
//        this.prod_unitof_measure = prod_unitof_measure;
//        this.prod_status = prod_status;
//        this.prod_soldby = prod_soldby;
//        this.prod_datetimeadded = prod_datetimeadded;
//        this.prod_price = prod_price;
//        this.prod_rop = prod_rop;
//        this.prod_stock = prod_stock;
//    }


    public Product(String prod_name, String prod_brand, String prod_unitof_measure, String prod_status, double prod_price, double prod_rop, int prod_stock, String prod_reference, double discounted_price) {
        this.prod_name = prod_name;
        this.prod_brand = prod_brand;
        this.prod_unitof_measure = prod_unitof_measure;
        this.prod_status = prod_status;
        this.prod_price = prod_price;
        this.prod_rop = prod_rop;
        this.prod_stock = prod_stock;
        this.prod_reference = prod_reference;
//        this.prod_disc_price = prod_disc_price;
        this.discounted_price = discounted_price;
    }

    public Category getCategory() {
        return category;
    }

    public Product(ProductCondition productCondition) {
        this.productCondition = productCondition;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
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

    public ProductCondition getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(ProductCondition productCondition) {
        this.productCondition = productCondition;
    }

    public int getProd_stock() {
        return prod_stock;
    }

    public void setProd_stock(int prod_stock) {
        this.prod_stock = prod_stock;
    }

    public String getProd_datetimeadded() {
        return prod_datetimeadded;
    }

    public void setProd_datetimeadded(String prod_datetimeadded) {
        this.prod_datetimeadded = prod_datetimeadded;
    }

    public String getProd_brand() {
        return prod_brand;
    }

    public void setProd_brand(String prod_brand) {
        this.prod_brand = prod_brand;
    }

    public String getProd_expdate() {
        return prod_expdate;
    }

    public void setProd_expdate(String prod_expdate) {
        this.prod_expdate = prod_expdate;
    }


    public QRCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }

    public ProductExpiration getProductExpiration() {
        return productExpiration;
    }

    public void setProductExpiration(ProductExpiration productExpiration) {
        this.productExpiration = productExpiration;
    }

    public int getProd_expdatecount() {
        return prod_expdatecount;
    }

    public void setProd_expdatecount(int prod_expdatecount) {
        this.prod_expdatecount = prod_expdatecount;
    }

    public String getProd_reference() {
        return prod_reference;
    }

    public void setProd_reference(String prod_reference) {
        this.prod_reference = prod_reference;
    }

    public double getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(double prod_qty) {
        this.prod_qty = prod_qty;
    }

    public double getProd_subtotal() {
        return prod_subtotal;
    }

    public void setProd_subtotal(double prod_subtotal) {
        this.prod_subtotal = prod_subtotal;
    }

//    public double getDiscounted_price() {
//        return prod_disc_price;
//    }
//
//    public void setDiscounted_price(double discounted_price) {
//        this.prod_disc_price = discounted_price;
//    }


    public double getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(double discounted_price) {
        this.discounted_price = discounted_price;
    }
}
