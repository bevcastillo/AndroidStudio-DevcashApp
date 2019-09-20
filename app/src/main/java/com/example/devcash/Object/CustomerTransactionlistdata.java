package com.example.devcash.Object;

import java.util.Map;

public class CustomerTransactionlistdata {


    int customer_id;
    double cash_received, change, subtotal, vat, vat_exempt_sale, senior_discount, amount_due, total_item_discount;
    int total_item_qty;
    String customer_email, customer_phone, transaction_datetime, customer_type, transaction_status;
    Enterprise enterprise;
    Employee employee;

    Map<String, Object> customer_cart;

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public double getCash_received() {
        return cash_received;
    }

    public void setCash_received(double cash_received) {
        this.cash_received = cash_received;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public double getVat_exempt_sale() {
        return vat_exempt_sale;
    }

    public void setVat_exempt_sale(double vat_exempt_sale) {
        this.vat_exempt_sale = vat_exempt_sale;
    }

    public double getSenior_discount() {
        return senior_discount;
    }

    public void setSenior_discount(double senior_discount) {
        this.senior_discount = senior_discount;
    }

    public double getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(double amount_due) {
        this.amount_due = amount_due;
    }

    public double getTotal_item_discount() {
        return total_item_discount;
    }

    public void setTotal_item_discount(double total_item_discount) {
        this.total_item_discount = total_item_discount;
    }

    public int getTotal_item_qty() {
        return total_item_qty;
    }

    public void setTotal_item_qty(int total_item_qty) {
        this.total_item_qty = total_item_qty;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getTransaction_datetime() {
        return transaction_datetime;
    }

    public void setTransaction_datetime(String transaction_datetime) {
        this.transaction_datetime = transaction_datetime;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Map<String, Object> getCustomer_cart() {
        return customer_cart;
    }

    public void setCustomer_cart(Map<String, Object> customer_cart) {
        this.customer_cart = customer_cart;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    //
//    int customer_id;
//    double total_qty, total_price, vat, cash_received, change, subtotal, total_discount;
//    String customer_email, customer_phone, transaction_datetime;
//    Enterprise enterprise;
//    Employee employee;
//
//    public int getCustomer_id() {
//        return customer_id;
//    }
//
//    public void setCustomer_id(int customer_id) {
//        this.customer_id = customer_id;
//    }
//
//    public double getTotal_qty() {
//        return total_qty;
//    }
//
//    public void setTotal_qty(double total_qty) {
//        this.total_qty = total_qty;
//    }
//
//    public double getTotal_price() {
//        return total_price;
//    }
//
//    public void setTotal_price(double total_price) {
//        this.total_price = total_price;
//    }
//
//    public double getVat() {
//        return vat;
//    }
//
//    public void setVat(double vat) {
//        this.vat = vat;
//    }
//
//    public double getCash_received() {
//        return cash_received;
//    }
//
//    public void setCash_received(double cash_received) {
//        this.cash_received = cash_received;
//    }
//
//    public double getChange() {
//        return change;
//    }
//
//    public void setChange(double change) {
//        this.change = change;
//    }
//
//    public double getSubtotal() {
//        return subtotal;
//    }
//
//    public void setSubtotal(double subtotal) {
//        this.subtotal = subtotal;
//    }
//
//    public String getCustomer_email() {
//        return customer_email;
//    }
//
//    public void setCustomer_email(String customer_email) {
//        this.customer_email = customer_email;
//    }
//
//    public String getCustomer_phone() {
//        return customer_phone;
//    }
//
//    public void setCustomer_phone(String customer_phone) {
//        this.customer_phone = customer_phone;
//    }
//
//    public String getTransaction_datetime() {
//        return transaction_datetime;
//    }
//
//    public void setTransaction_datetime(String transaction_datetime) {
//        this.transaction_datetime = transaction_datetime;
//    }
//
//    public Enterprise getEnterprise() {
//        return enterprise;
//    }
//
//    public void setEnterprise(Enterprise enterprise) {
//        this.enterprise = enterprise;
//    }
//
//    public Employee getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(Employee employee) {
//        this.employee = employee;
//    }
//
//    public double getTotal_discount() {
//        return total_discount;
//    }
//
//    public void setTotal_discount(double total_discount) {
//        this.total_discount = total_discount;
//    }
}
