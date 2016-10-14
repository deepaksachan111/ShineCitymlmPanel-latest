package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/16/2015.
 */
public class PaymentTypeData {
    private String paymenttypeid;
    private String paymenttypevalue;

    public PaymentTypeData(String paymenttypeid, String paymenttypevalue) {
        this.paymenttypeid = paymenttypeid;
        this.paymenttypevalue = paymenttypevalue;
    }

    public String getPaymenttypeid() {
        return paymenttypeid;
    }

    public void setPaymenttypeid(String paymenttypeid) {
        this.paymenttypeid = paymenttypeid;
    }

    public String getPaymenttypevalue() {
        return paymenttypevalue;
    }

    public void setPaymenttypevalue(String paymenttypevalue) {
        this.paymenttypevalue = paymenttypevalue;
    }
}
