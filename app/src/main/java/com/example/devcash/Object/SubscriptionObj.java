package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class SubscriptionObj {
    private int payment_id;
    private String subscription_type, subscription_value, subsription_amt;

    public SubscriptionObj(int payment_id, String subscription_type, String subscription_value,
                           String subsription_amt) {
        this.payment_id = payment_id;
        this.subscription_type = subscription_type;
        this.subscription_value = subscription_value;
        this.subsription_amt = subsription_amt;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public String getSubscription_value() {
        return subscription_value;
    }

    public void setSubscription_value(String subscription_value) {
        this.subscription_value = subscription_value;
    }

    public String getSubsription_amt() {
        return subsription_amt;
    }

    public void setSubsription_amt(String subsription_amt) {
        this.subsription_amt = subsription_amt;
    }
}
