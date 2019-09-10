package com.example.devcash.Object;

public class Productlistdata {
    String prod_name, prod_brand, prod_unitof_measure, prod_status, category_name, cond_name, prod_expdate, prod_reference;
    double prod_price, prod_rop, subtotal, prod_disc_price;
    int prod_stock, prod_expdatecount, prod_qty, prodclick;
    public Category category;
    public ProductCondition productCondition;
    public Discount discount;
    public QRCode qrCode;
    public ProductExpiration productExpiration;


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


    public int getProd_expdatecount() {
        return prod_expdatecount;
    }

    public void setProd_expdatecount(int prod_expdatecount) {
        this.prod_expdatecount = prod_expdatecount;
    }

    public ProductExpiration getProductExpiration() {
        return productExpiration;
    }

    public void setProductExpiration(ProductExpiration productExpiration) {
        this.productExpiration = productExpiration;
    }

    public int getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(int prod_qty) {
        this.prod_qty = prod_qty;
    }

    public int getProdclick() {
        return prodclick;
    }

    public void setProdclick(int prodclick) {
        this.prodclick = prodclick;
    }

    public String getProd_reference() {
        return prod_reference;
    }

    public void setProd_reference(String prod_reference) {
        this.prod_reference = prod_reference;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductCondition getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(ProductCondition productCondition) {
        this.productCondition = productCondition;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public QRCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QRCode qrCode) {
        this.qrCode = qrCode;
    }

    public double getProd_disc_price() {
        return prod_disc_price;
    }

    public void setProd_disc_price(double prod_disc_price) {
        this.prod_disc_price = prod_disc_price;
    }
}
