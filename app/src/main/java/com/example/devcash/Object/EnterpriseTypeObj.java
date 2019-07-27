package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class EnterpriseTypeObj {
    private int ent_id, ent_tot_no_emp;
    private String ent_cat;

    public EnterpriseTypeObj(int ent_id, int ent_tot_no_emp, String ent_cat) {
        this.ent_id = ent_id;
        this.ent_tot_no_emp = ent_tot_no_emp;
        this.ent_cat = ent_cat;
    }

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public int getEnt_tot_no_emp() {
        return ent_tot_no_emp;
    }

    public void setEnt_tot_no_emp(int ent_tot_no_emp) {
        this.ent_tot_no_emp = ent_tot_no_emp;
    }

    public String getEnt_cat() {
        return ent_cat;
    }

    public void setEnt_cat(String ent_cat) {
        this.ent_cat = ent_cat;
    }
}
