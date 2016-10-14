package com.lucknow.quaere.shinecity_mlmpanel.Model;



/**
 * Created by deepak sachan on 11/16/2015.
 */
public class BusinessOldReportData {

    private String date;
    private String membername;
    private String memberid;
    private String paymenttype;
    private String mode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;
    private String status;

    public BusinessOldReportData(String date, String membername, String memberid, String paymenttype, String mode, String amount, String status) {
        this.date = date;
        this.membername = membername;
        this.memberid = memberid;
        this.paymenttype = paymenttype;
        this.mode = mode;
        this.amount = amount;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
