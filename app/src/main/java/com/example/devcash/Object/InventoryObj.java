package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class InventoryObj {
    private int inventory_id, prod_id, inventory_stock;
    private String inventory_date_added, inventory_date_updated;

    public InventoryObj(int inventory_id, int prod_id, int inventory_stock, String inventory_date_added,
                        String inventory_date_updated) {
        this.inventory_id = inventory_id;
        this.prod_id = prod_id;
        this.inventory_stock = inventory_stock;
        this.inventory_date_added = inventory_date_added;
        this.inventory_date_updated = inventory_date_updated;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getInventory_stock() {
        return inventory_stock;
    }

    public void setInventory_stock(int inventory_stock) {
        this.inventory_stock = inventory_stock;
    }

    public String getInventory_date_added() {
        return inventory_date_added;
    }

    public void setInventory_date_added(String inventory_date_added) {
        this.inventory_date_added = inventory_date_added;
    }

    public String getInventory_date_updated() {
        return inventory_date_updated;
    }

    public void setInventory_date_updated(String inventory_date_updated) {
        this.inventory_date_updated = inventory_date_updated;
    }
}
