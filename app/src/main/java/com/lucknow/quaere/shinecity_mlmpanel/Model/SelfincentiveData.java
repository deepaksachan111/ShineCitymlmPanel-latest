package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 10/6/2015.
 */
public class SelfincentiveData {
    private String Payoutdate;
    private String PayoutValue;
     public SelfincentiveData(String payoutdate, String id){
          this.Payoutdate = payoutdate;
         this.PayoutValue = id;
     }
 public SelfincentiveData(){

}
    public String getPayoutdate() {
        return Payoutdate;
    }

    public void setPayoutdate(String payoutdate) {
        Payoutdate = payoutdate;
    }

    public String getPayoutValue() {
        return PayoutValue;
    }

    public void setPayoutValue(String payoutValue) {
        PayoutValue = payoutValue;
    }
}
