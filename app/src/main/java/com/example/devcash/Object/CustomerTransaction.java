package com.example.devcash.Object;

import java.util.Map;

public class CustomerTransaction {

    int customer_id;
//    double total_price, vat, cash_received, change, subtotal, total_discount, vat_exempt, senior_discount, senior_sale_withVat;
//    int total_qty;

    double cash_received, change, subtotal, vat, vat_exempt_sale, senior_discount, amount_due, total_item_discount;
    int total_item_qty;
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

//    public int getTotal_qty() {
//        return total_qty;
//    }
//
//    public void setTotal_qty(int total_qty) {
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
//    public double getTotal_discount() {
//        return total_discount;
//    }
//
//    public void setTotal_discount(double total_discount) {
//        this.total_discount = total_discount;
//    }
//
//    public String getCustomer_type() {
//        return customer_type;
//    }
//
//    public void setCustomer_type(String customer_type) {
//        this.customer_type = customer_type;
//    }
//
//    public double getVat_exempt() {
//        return vat_exempt;
//    }
//
//    public void setVat_exempt(double vat_exempt) {
//        this.vat_exempt = vat_exempt;
//    }
//
//    public double getSenior_discount() {
//        return senior_discount;
//    }
//
//    public void setSenior_discount(double senior_discount) {
//        this.senior_discount = senior_discount;
//    }
//
//    public double getSenior_sale_withVat() {
//        return senior_sale_withVat;
//    }
//
//    public void setSenior_sale_withVat(double senior_sale_withVat) {
//        this.senior_sale_withVat = senior_sale_withVat;
//    }


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

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }
}
