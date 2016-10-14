package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/19/2015.
 */
public class PayoutDetailData {
    private  String date;
    private  String paymentdetail;
    private  String harmonyallot;
    private  String harmony;
    private  String  selfincentive;
    private  String grassamt;
    private  String tds;
    private  String processing;
    private  String netamount;
    private  String deduction;

    public PayoutDetailData(String date, String paymentdetail, String harmonyallot, String harmony, String selfincentive, String grassamt, String tds, String processing, String netamount) {
        this.date = date;
        this.paymentdetail = paymentdetail;
        this.harmonyallot = harmonyallot;
        this.harmony = harmony;
        this.selfincentive = selfincentive;
        this.grassamt = grassamt;
        this.tds = tds;
        this.processing = processing;
        this.netamount = netamount;
    }

    public PayoutDetailData(String date, String grassamt, String deduction, String netamount) {
        this.date = date;
        this.grassamt = grassamt;
        this.deduction = deduction;
        this.netamount = netamount;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentdetail() {
        return paymentdetail;
    }

    public void setPaymentdetail(String paymentdetail) {
        this.paymentdetail = paymentdetail;
    }

    public String getHarmonyallot() {
        return harmonyallot;
    }

    public void setHarmonyallot(String harmonyallot) {
        this.harmonyallot = harmonyallot;
    }

    public String getHarmony() {
        return harmony;
    }

    public void setHarmony(String harmony) {
        this.harmony = harmony;
    }

    public String getSelfincentive() {
        return selfincentive;
    }

    public void setSelfincentive(String selfincentive) {
        this.selfincentive = selfincentive;
    }

    public String getGrassamt() {
        return grassamt;
    }

    public void setGrassamt(String grassamt) {
        this.grassamt = grassamt;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public String getNetamount() {
        return netamount;
    }

    public void setNetamount(String netamount) {
        this.netamount = netamount;
    }
}
