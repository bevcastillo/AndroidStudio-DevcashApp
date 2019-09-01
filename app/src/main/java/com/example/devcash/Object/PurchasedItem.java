package com.example.devcash.Object;

public class PurchasedItem {
    private Services services;
    private Product product;
    private PurchaseTransaction purchaseTransaction;


    public PurchasedItem() {
    }

    public PurchasedItem(Services services, Product product) {
        this.services = services;
        this.product = product;
    }

    public Services getServices() {
        return services;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
