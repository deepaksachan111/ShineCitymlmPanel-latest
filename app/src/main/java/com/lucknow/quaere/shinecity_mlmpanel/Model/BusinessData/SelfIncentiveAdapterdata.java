package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/23/2015.
 */
public class SelfIncentiveAdapterdata {

    private String fromid;
    private String membername;
    private String currentdate;
    private String payoutno;
    private String closingdate;
    private String amount;

    public SelfIncentiveAdapterdata(String currentdate, String payoutno, String closingdate, String amount) {
        this.currentdate = currentdate;
        this.payoutno = payoutno;
        this.closingdate = closingdate;
        this.amount = amount;
    }

    public SelfIncentiveAdapterdata(String fromid, String membername, String currentdate, String payoutno, String closingdate, String amount) {
        this.fromid = fromid;
        this.membername = membername;
        this.currentdate = currentdate;
        this.payoutno = payoutno;
        this.closingdate = closingdate;
        this.amount = amount;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getPayoutno() {
        return payoutno;
    }

    public void setPayoutno(String payoutno) {
        this.payoutno = payoutno;
    }

    public String getClosingdate() {
        return closingdate;
    }

    public void setClosingdate(String closingdate) {
        this.closingdate = closingdate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
