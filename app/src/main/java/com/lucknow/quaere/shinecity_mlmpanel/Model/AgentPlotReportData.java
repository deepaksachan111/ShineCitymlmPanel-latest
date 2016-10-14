package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/18/2015.
 */
public class AgentPlotReportData {

    private String plotholder;
    private String member;
    private String depositedby;
    private String plotno;
    private String recepit;
    private String pmtdate;
    private String mode;
    private String status;
    private String pmttype;
    private String amount;

    public AgentPlotReportData(String plotholder, String member, String depositedby, String plotno, String recepit, String mode, String pmttype, String amount,String status) {
        this.plotholder = plotholder;
        this.member = member;
        this.depositedby = depositedby;
        this.plotno = plotno;
        this.recepit = recepit;
        this.mode = mode;
        this.status = status;
        this.pmttype = pmttype;
        this.amount = amount;
    }

    public String getPlotholder() {
        return plotholder;
    }

    public void setPlotholder(String plotholder) {
        this.plotholder = plotholder;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getDepositedby() {
        return depositedby;
    }

    public void setDepositedby(String depositedby) {
        this.depositedby = depositedby;
    }

    public String getPlotno() {
        return plotno;
    }

    public void setPlotno(String plotno) {
        this.plotno = plotno;
    }

    public String getRecepit() {
        return recepit;
    }

    public void setRecepit(String recepit) {
        this.recepit = recepit;
    }

    public String getPmtdate() {
        return pmtdate;
    }

    public void setPmtdate(String pmtdate) {
        this.pmtdate = pmtdate;
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

    public String getPmttype() {
        return pmttype;
    }

    public void setPmttype(String pmttype) {
        this.pmttype = pmttype;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
