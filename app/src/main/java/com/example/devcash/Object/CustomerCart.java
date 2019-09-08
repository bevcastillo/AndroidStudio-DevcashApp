package com.example.devcash.Object;

public class CustomerCart {

//    double total_qty, total_price, vat, cash_received, change;
    Product product;
    Services services;
    Employee employee;
    Enterprise enterprise;
    Item item;

    public CustomerCart() {
    }

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
