package com.lucknow.quaere.shinecity_mlmpanel.Model;

/**
 * Created by deepak sachan on 11/6/2015.
 */
public class ModelData {

    private  String userid;
    private  String displayname;

    public ModelData(String id, String name){
        this.userid =id;
        this.displayname=name;

    }
    public void setUserid(String userid){
        this.userid= userid;
    }
    public String getUserid(){
        return userid;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
