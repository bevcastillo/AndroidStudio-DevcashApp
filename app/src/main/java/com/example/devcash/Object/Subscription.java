package com.example.devcash.Object;

public class Subscription {
    String subs_datetime_given, subs_startdate, subs_enddate, pay_due, pay_status, subs_type;
    double pay_tot_amt, pay_prev_bal, subs_cost;
    Owner owner;
    Enterprise enterprise;

    public Subscription(String subs_datetime_given, String subs_startdate, String subs_enddate, String pay_due,
                        String pay_status, String subs_type, double pay_tot_amt, double pay_prev_bal, double subs_cost) {
        this.subs_datetime_given = subs_datetime_given;
        this.subs_startdate = subs_startdate;
        this.subs_enddate = subs_enddate;
        this.pay_due = pay_due;
        this.pay_status = pay_status;
        this.subs_type = subs_type;
        this.pay_tot_amt = pay_tot_amt;
        this.pay_prev_bal = pay_prev_bal;
        this.subs_cost = subs_cost;
    }

    public String getSubs_datetime_given() {
        return subs_datetime_given;
    }

    public void setSubs_datetime_given(String subs_datetime_given) {
        this.subs_datetime_given = subs_datetime_given;
    }

    public String getSubs_startdate() {
        return subs_startdate;
    }

    public void setSubs_startdate(String subs_startdate) {
        this.subs_startdate = subs_startdate;
    }

    public String getSubs_enddate() {
        return subs_enddate;
    }

    public void setSubs_enddate(String subs_enddate) {
        this.subs_enddate = subs_enddate;
    }

    public String getPay_due() {
        return pay_due;
    }

    public void setPay_due(String pay_due) {
        this.pay_due = pay_due;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getSubs_type() {
        return subs_type;
    }

    public void setSubs_type(String subs_type) {
        this.subs_type = subs_type;
    }

    public double getPay_tot_amt() {
        return pay_tot_amt;
    }

    public void setPay_tot_amt(double pay_tot_amt) {
        this.pay_tot_amt = pay_tot_amt;
    }

    public double getPay_prev_bal() {
        return pay_prev_bal;
    }

    public void setPay_prev_bal(double pay_prev_bal) {
        this.pay_prev_bal = pay_prev_bal;
    }

    public double getSubs_cost() {
        return subs_cost;
    }

    public void setSubs_cost(double subs_cost) {
        this.subs_cost = subs_cost;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
}
