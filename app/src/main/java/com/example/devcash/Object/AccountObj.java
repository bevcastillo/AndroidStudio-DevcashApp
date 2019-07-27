package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class AccountObj {
    private int acct_id;
    private String acct_uname, acct_email, acct_passw, acct_type, acct_status;

    public AccountObj(int acct_id, String acct_uname, String acct_email, String acct_passw,
                      String acct_type, String acct_status) {
        this.acct_id = acct_id;
        this.acct_uname = acct_uname;
        this.acct_email = acct_email;
        this.acct_passw = acct_passw;
        this.acct_type = acct_type;
        this.acct_status = acct_status;
    }

    public int getAcct_id() {
        return acct_id;
    }

    public void setAcct_id(int acct_id) {
        this.acct_id = acct_id;
    }

    public String getAcct_uname() {
        return acct_uname;
    }

    public void setAcct_uname(String acct_uname) {
        this.acct_uname = acct_uname;
    }

    public String getAcct_email() {
        return acct_email;
    }

    public void setAcct_email(String acct_email) {
        this.acct_email = acct_email;
    }

    public String getAcct_passw() {
        return acct_passw;
    }

    public void setAcct_passw(String acct_passw) {
        this.acct_passw = acct_passw;
    }

    public String getAcct_type() {
        return acct_type;
    }

    public void setAcct_type(String acct_type) {
        this.acct_type = acct_type;
    }

    public String getAcct_status() {
        return acct_status;
    }

    public void setAcct_status(String acct_status) {
        this.acct_status = acct_status;
    }
}
