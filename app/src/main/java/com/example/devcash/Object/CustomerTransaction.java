package com.example.devcash.Object;

import java.util.Map;

public class CustomerTransaction {

    int customer_id;
    double total_price, vat, cash_received, change, subtotal, total_discount;
    int total_qty;
    String customer_email, customer_phone, transaction_datetime, customer_type;
    Enterprise enterprise;
    Employee employee;

//    CustomerCart customerCart;
//    Map<String, CustomerCart> customer_cart;
    Map<String, Object> customer_cart;

    public CustomerTransaction() {
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
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

//    public CustomerCart getCustomerCart() {
//        return customerCart;
//    }
//
//    public void setCustomerCart(CustomerCart customerCart) {
//        this.customerCart = customerCart;
//    }


//    public Map<String, CustomerCart> getCustomer_cart() {
//        return customer_cart;
//    }
//
//    public void setCustomer_cart(Map<String, CustomerCart> customer_cart) {
//        this.customer_cart = customer_cart;
//    }


    public Map<String, Object> getCustomer_cart() {
        return customer_cart;
    }

    public void setCustomer_cart(Map<String, Object> customer_cart) {
        this.customer_cart = customer_cart;
    }

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
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

    public double getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(double total_discount) {
        this.total_discount = total_discount;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }
}
