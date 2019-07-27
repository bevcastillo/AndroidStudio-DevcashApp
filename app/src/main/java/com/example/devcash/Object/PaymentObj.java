package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class PaymentObj {
    private int payment_id, owner_id;
    private String payment_datetime_given, payment_start_date, payment_end_date, payment_due_date, payment_status;
    private double payment_tot_amt, payment_prev_balance;

    public PaymentObj(int payment_id, int owner_id, String payment_datetime_given, String payment_start_date,
                      String payment_end_date, String payment_due_date, String payment_status,
                      double payment_tot_amt, double payment_prev_balance) {
        this.payment_id = payment_id;
        this.owner_id = owner_id;
        this.payment_datetime_given = payment_datetime_given;
        this.payment_start_date = payment_start_date;
        this.payment_end_date = payment_end_date;
        this.payment_due_date = payment_due_date;
        this.payment_status = payment_status;
        this.payment_tot_amt = payment_tot_amt;
        this.payment_prev_balance = payment_prev_balance;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getPayment_datetime_given() {
        return payment_datetime_given;
    }

    public void setPayment_datetime_given(String payment_datetime_given) {
        this.payment_datetime_given = payment_datetime_given;
    }

    public String getPayment_start_date() {
        return payment_start_date;
    }

    public void setPayment_start_date(String payment_start_date) {
        this.payment_start_date = payment_start_date;
    }

    public String getPayment_end_date() {
        return payment_end_date;
    }

    public void setPayment_end_date(String payment_end_date) {
        this.payment_end_date = payment_end_date;
    }

    public String getPayment_due_date() {
        return payment_due_date;
    }

    public void setPayment_due_date(String payment_due_date) {
        this.payment_due_date = payment_due_date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public double getPayment_tot_amt() {
        return payment_tot_amt;
    }

    public void setPayment_tot_amt(double payment_tot_amt) {
        this.payment_tot_amt = payment_tot_amt;
    }

    public double getPayment_prev_balance() {
        return payment_prev_balance;
    }

    public void setPayment_prev_balance(double payment_prev_balance) {
        this.payment_prev_balance = payment_prev_balance;
    }
}
