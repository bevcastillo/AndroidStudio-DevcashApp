package com.example.devcash.Object;

public class Owner {
    String owner_lname, owner_fname, owner_username;
    Account account;
    Enterprise enterprise;


    public Owner() {
    }

    public Owner(String owner_lname, String owner_fname, String owner_username, Account account, Enterprise enterprise) {
        this.owner_lname = owner_lname;
        this.owner_fname = owner_fname;
        this.owner_username = owner_username;
        this.account = account;
        this.enterprise = enterprise;
    }

    public String getOwner_lname() {
        return owner_lname;
    }

    public void setOwner_lname(String owner_lname) {
        this.owner_lname = owner_lname;
    }

    public String getOwner_fname() {
        return owner_fname;
    }

    public void setOwner_fname(String owner_fname) {
        this.owner_fname = owner_fname;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
