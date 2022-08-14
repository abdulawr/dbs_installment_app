package com.ss_technology.hanguoilproject.Container;

public class PaymentContainer {
    String id,amount,date,type,tran_type,appID,expence_type,adminIDS;

    public String getAdminIDS() {
        return adminIDS;
    }

    public void setAdminIDS(String adminIDS) {
        this.adminIDS = adminIDS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTran_type() {
        return tran_type;
    }

    public void setTran_type(String tran_type) {
        this.tran_type = tran_type;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getExpence_type() {
        return expence_type;
    }

    public void setExpence_type(String expence_type) {
        this.expence_type = expence_type;
    }
}
