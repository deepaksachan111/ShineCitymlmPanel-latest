package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/16/2015.
 */
public class PaymentProceedData {
    private String paymentproceedid;
    private String paymentproceedvalue;

    public PaymentProceedData(String paymentproceedid, String paymentproceedvalue) {
        this.paymentproceedid = paymentproceedid;
        this.paymentproceedvalue = paymentproceedvalue;
    }

    public String getPaymentproceedid() {
        return paymentproceedid;
    }

    public void setPaymentproceedid(String paymentproceedid) {
        this.paymentproceedid = paymentproceedid;
    }

    public String getPaymentproceedvalue() {
        return paymentproceedvalue;
    }

    public void setPaymentproceedvalue(String paymentproceedvalue) {
        this.paymentproceedvalue = paymentproceedvalue;
    }
}
