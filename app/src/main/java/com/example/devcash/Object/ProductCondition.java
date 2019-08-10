package com.example.devcash.Object;

public class ProductCondition {
    String cond_name;
    int cond_count;

    public ProductCondition() {
    }

    public ProductCondition(String cond_name, int cond_count) {
        this.cond_name = cond_name;
        this.cond_count = cond_count;
    }

    public String getCond_name() {
        return cond_name;
    }

    public void setCond_name(String cond_name) {
        this.cond_name = cond_name;
    }

    public int getCond_count() {
        return cond_count;
    }

    public void setCond_count(int cond_count) {
        this.cond_count = cond_count;
    }
}
