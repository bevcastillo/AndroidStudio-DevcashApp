package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class EnterpriseObj {
    private int ent_id, owner_id, ent_telno;
    private String ent_name, ent_industry_type,ent_addr, ent_bsnspermit, ent_tin;

    public EnterpriseObj(int ent_id, int owner_id, int ent_telno, String ent_name,
                         String ent_industry_type, String ent_addr, String ent_bsnspermit,
                         String ent_tin) {
        this.ent_id = ent_id;
        this.owner_id = owner_id;
        this.ent_telno = ent_telno;
        this.ent_name = ent_name;
        this.ent_industry_type = ent_industry_type;
        this.ent_addr = ent_addr;
        this.ent_bsnspermit = ent_bsnspermit;
        this.ent_tin = ent_tin;
    }

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getEnt_telno() {
        return ent_telno;
    }

    public void setEnt_telno(int ent_telno) {
        this.ent_telno = ent_telno;
    }

    public String getEnt_name() {
        return ent_name;
    }

    public void setEnt_name(String ent_name) {
        this.ent_name = ent_name;
    }

    public String getEnt_industry_type() {
        return ent_industry_type;
    }

    public void setEnt_industry_type(String ent_industry_type) {
        this.ent_industry_type = ent_industry_type;
    }

    public String getEnt_addr() {
        return ent_addr;
    }

    public void setEnt_addr(String ent_addr) {
        this.ent_addr = ent_addr;
    }

    public String getEnt_bsnspermit() {
        return ent_bsnspermit;
    }

    public void setEnt_bsnspermit(String ent_bsnspermit) {
        this.ent_bsnspermit = ent_bsnspermit;
    }

    public String getEnt_tin() {
        return ent_tin;
    }

    public void setEnt_tin(String ent_tin) {
        this.ent_tin = ent_tin;
    }
}
