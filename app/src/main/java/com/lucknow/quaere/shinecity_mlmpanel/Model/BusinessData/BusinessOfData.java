package com.lucknow.quaere.shinecity_mlmpanel.Model.BusinessData;

/**
 * Created by deepak sachan on 11/16/2015.
 */
public class BusinessOfData {
    private String businessid;
    private String businessvalue;

    public BusinessOfData(String businessid, String businessvalue) {
        this.businessid = businessid;
        this.businessvalue = businessvalue;
    }




    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getBusinessvalue() {
        return businessvalue;
    }

    public void setBusinessvalue(String businessvalue) {
        this.businessvalue = businessvalue;
    }
}
