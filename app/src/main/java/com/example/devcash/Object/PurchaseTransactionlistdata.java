package com.example.devcash.Object;

public class PurchaseTransactionlistdata {
    String purch_datetime, cust_type, cust_phone, cust_email;
    double purch_qty, purch_tot_qty, purch_subtotal, purch_tot_price, purch_vat, purch_vatE, purch_zero, cust_cash, cust_change;
    Product product;
    Services services;
    Employee employee;
    Enterprise enterprise;
    PurchasedItem purchasedItem;

    public String getPurch_datetime() {
        return purch_datetime;
    }

    public void setPurch_datetime(String purch_datetime) {
        this.purch_datetime = purch_datetime;
    }

    public String getCust_type() {
        return cust_type;
    }

    public void setCust_type(String cust_type) {
        this.cust_type = cust_type;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public double getPurch_qty() {
        return purch_qty;
    }

    public void setPurch_qty(double purch_qty) {
        this.purch_qty = purch_qty;
    }

    public double getPurch_tot_qty() {
        return purch_tot_qty;
    }

    public void setPurch_tot_qty(double purch_tot_qty) {
        this.purch_tot_qty = purch_tot_qty;
    }

    public double getPurch_tot_price() {
        return purch_tot_price;
    }

    public void setPurch_tot_price(double purch_tot_price) {
        this.purch_tot_price = purch_tot_price;
    }

    public double getPurch_vat() {
        return purch_vat;
    }

    public void setPurch_vat(double purch_vat) {
        this.purch_vat = purch_vat;
    }

    public double getPurch_vatE() {
        return purch_vatE;
    }

    public void setPurch_vatE(double purch_vatE) {
        this.purch_vatE = purch_vatE;
    }

    public double getPurch_zero() {
        return purch_zero;
    }

    public void setPurch_zero(double purch_zero) {
        this.purch_zero = purch_zero;
    }

    public double getCust_cash() {
        return cust_cash;
    }

    public void setCust_cash(double cust_cash) {
        this.cust_cash = cust_cash;
    }

    public double getCust_change() {
        return cust_change;
    }

    public void setCust_change(double cust_change) {
        this.cust_change = cust_change;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public double getPurch_subtotal() {
        return purch_subtotal;
    }

    public void setPurch_subtotal(double purch_subtotal) {
        this.purch_subtotal = purch_subtotal;
    }

    public PurchasedItem getPurchasedItem() {
        return purchasedItem;
    }

    public void setPurchasedItem(PurchasedItem purchasedItem) {
        this.purchasedItem = purchasedItem;
    }
}
