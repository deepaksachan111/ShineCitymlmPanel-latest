package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/7/2015.
 */
public class DownlineRecordData {

    String membername;
    String memberid;
    String parentname;
    String pareientid;
    String tatalamt;
    String status;
    String date;


    public DownlineRecordData(String date, String membername, String memberid, String parentname, String pareientid, String tatalamt, String status) {
        this.date = date;
        this.membername = membername;
        this.memberid = memberid;
        this.parentname = parentname;
        this.pareientid = pareientid;
        this.tatalamt = tatalamt;
        this.status = status;
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

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getPareientid() {
        return pareientid;
    }

    public void setPareientid(String pareientid) {
        this.pareientid = pareientid;
    }

    public String getTatalamt() {
        return tatalamt;
    }

    public void setTatalamt(String tatalamt) {
        this.tatalamt = tatalamt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
