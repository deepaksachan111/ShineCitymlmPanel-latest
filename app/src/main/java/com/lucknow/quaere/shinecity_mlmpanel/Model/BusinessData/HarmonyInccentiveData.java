package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/26/2015.
 */
public class HarmonyInccentiveData {
    private String currentdate;
    private String payoutno;
    private String closingdate;
    private String amount;

    public HarmonyInccentiveData(String currentdate, String payoutno, String closingdate, String amount) {
        this.currentdate = currentdate;
        this.payoutno = payoutno;
        this.closingdate = closingdate;
        this.amount = amount;
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
