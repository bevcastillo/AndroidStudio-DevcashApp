package com.example.devcash.Model;

public class InventoryList {

    private int icon;
    private String inventory_title;

    //constructor

    public InventoryList(int icon,String inventory_title) {
        super();
        this.icon = icon;
        this.inventory_title = inventory_title;
    }

    //getters and setters
    public String getInventory_title() {
        return inventory_title;
    }

    public void setInventory_title(String inventory_title) {
        this.inventory_title = inventory_title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
