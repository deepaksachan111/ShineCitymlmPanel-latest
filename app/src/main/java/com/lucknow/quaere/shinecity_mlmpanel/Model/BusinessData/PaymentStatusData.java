package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/16/2015.
 */
public class PaymentStatusData {

    private String paymentstatusid;
    private String paymentstatusvalue;

    public PaymentStatusData(String paymentstatusid, String paymentstatusvalue) {
        this.paymentstatusid = paymentstatusid;
        this.paymentstatusvalue = paymentstatusvalue;
    }

    public String getPaymentstatusid() {
        return paymentstatusid;
    }

    public void setPaymentstatusid(String paymentstatusid) {
        this.paymentstatusid = paymentstatusid;
    }

    public String getPaymentstatusvalue() {
        return paymentstatusvalue;
    }

    public void setPaymentstatusvalue(String paymentstatusvalue) {
        this.paymentstatusvalue = paymentstatusvalue;
    }
}
