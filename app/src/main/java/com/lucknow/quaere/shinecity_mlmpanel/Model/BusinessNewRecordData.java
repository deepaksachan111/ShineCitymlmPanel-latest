package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/17/2015.
 */
public class BusinessNewRecordData {


        private String date;
        private String custid;
        private String agentid;
        private String paymenttype;
        private String mode;
        private String site;
        private String plot;
        private String amount;
        private String status;

        public BusinessNewRecordData(String date, String custid, String agentid, String paymenttype, String mode,String site,String plot, String amount, String status) {
            this.date = date;
            this.custid = custid;
            this.agentid = agentid;
            this.paymenttype = paymenttype;
            this.mode = mode;
            this.site =site;
            this.plot =plot;
            this.amount = amount;
            this.status = status;
        }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


