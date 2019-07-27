package com.example.devcash.Object;

public class PurchaseTransactionObj {
    private int trans_id, prod_id, service_id, emp_id, ent_id, trans_cust_ctcnum,
            trans_purchase_qty, trans_tot_purchase_qty;
    private String trans_cust_type, trans_cust_email, trans_datetime;
    private double trans_purchaseqty_amt, trans_tot_price, trans_VATable_sales, trans_VAT_amt,
            trans_VAT_value, trans_VATExempt, trans_zero_rated;

    public PurchaseTransactionObj(int trans_id, int prod_id, int service_id, int emp_id, int ent_id,
                                  int trans_cust_ctcnum, int trans_purchase_qty, int trans_tot_purchase_qty,
                                  String trans_cust_type, String trans_cust_email, String trans_datetime,
                                  double trans_purchaseqty_amt, double trans_tot_price,
                                  double trans_VATable_sales, double trans_VAT_amt, double trans_VAT_value,
                                  double trans_VATExempt, double trans_zero_rated) {
        this.trans_id = trans_id;
        this.prod_id = prod_id;
        this.service_id = service_id;
        this.emp_id = emp_id;
        this.ent_id = ent_id;
        this.trans_cust_ctcnum = trans_cust_ctcnum;
        this.trans_purchase_qty = trans_purchase_qty;
        this.trans_tot_purchase_qty = trans_tot_purchase_qty;
        this.trans_cust_type = trans_cust_type;
        this.trans_cust_email = trans_cust_email;
        this.trans_datetime = trans_datetime;
        this.trans_purchaseqty_amt = trans_purchaseqty_amt;
        this.trans_tot_price = trans_tot_price;
        this.trans_VATable_sales = trans_VATable_sales;
        this.trans_VAT_amt = trans_VAT_amt;
        this.trans_VAT_value = trans_VAT_value;
        this.trans_VATExempt = trans_VATExempt;
        this.trans_zero_rated = trans_zero_rated;
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public int getTrans_cust_ctcnum() {
        return trans_cust_ctcnum;
    }

    public void setTrans_cust_ctcnum(int trans_cust_ctcnum) {
        this.trans_cust_ctcnum = trans_cust_ctcnum;
    }

    public int getTrans_purchase_qty() {
        return trans_purchase_qty;
    }

    public void setTrans_purchase_qty(int trans_purchase_qty) {
        this.trans_purchase_qty = trans_purchase_qty;
    }

    public int getTrans_tot_purchase_qty() {
        return trans_tot_purchase_qty;
    }

    public void setTrans_tot_purchase_qty(int trans_tot_purchase_qty) {
        this.trans_tot_purchase_qty = trans_tot_purchase_qty;
    }

    public String getTrans_cust_type() {
        return trans_cust_type;
    }

    public void setTrans_cust_type(String trans_cust_type) {
        this.trans_cust_type = trans_cust_type;
    }

    public String getTrans_cust_email() {
        return trans_cust_email;
    }

    public void setTrans_cust_email(String trans_cust_email) {
        this.trans_cust_email = trans_cust_email;
    }

    public String getTrans_datetime() {
        return trans_datetime;
    }

    public void setTrans_datetime(String trans_datetime) {
        this.trans_datetime = trans_datetime;
    }

    public double getTrans_purchaseqty_amt() {
        return trans_purchaseqty_amt;
    }

    public void setTrans_purchaseqty_amt(double trans_purchaseqty_amt) {
        this.trans_purchaseqty_amt = trans_purchaseqty_amt;
    }

    public double getTrans_tot_price() {
        return trans_tot_price;
    }

    public void setTrans_tot_price(double trans_tot_price) {
        this.trans_tot_price = trans_tot_price;
    }

    public double getTrans_VATable_sales() {
        return trans_VATable_sales;
    }

    public void setTrans_VATable_sales(double trans_VATable_sales) {
        this.trans_VATable_sales = trans_VATable_sales;
    }

    public double getTrans_VAT_amt() {
        return trans_VAT_amt;
    }

    public void setTrans_VAT_amt(double trans_VAT_amt) {
        this.trans_VAT_amt = trans_VAT_amt;
    }

    public double getTrans_VAT_value() {
        return trans_VAT_value;
    }

    public void setTrans_VAT_value(double trans_VAT_value) {
        this.trans_VAT_value = trans_VAT_value;
    }

    public double getTrans_VATExempt() {
        return trans_VATExempt;
    }

    public void setTrans_VATExempt(double trans_VATExempt) {
        this.trans_VATExempt = trans_VATExempt;
    }

    public double getTrans_zero_rated() {
        return trans_zero_rated;
    }

    public void setTrans_zero_rated(double trans_zero_rated) {
        this.trans_zero_rated = trans_zero_rated;
    }
}
