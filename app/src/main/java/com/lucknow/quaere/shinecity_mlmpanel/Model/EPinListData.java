package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/20/2015.
 */
public class EPinListData {
    private String createddate;
    private String enpinno;
    private String owner;
    private String status;

    public EPinListData(String createddate, String enpinno, String owner, String status) {
        this.createddate = createddate;
        this.enpinno = enpinno;
        this.owner = owner;
        this.status = status;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getEnpinno() {
        return enpinno;
    }

    public void setEnpinno(String enpinno) {
        this.enpinno = enpinno;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
