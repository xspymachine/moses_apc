package com.xpluscloud.mosesshell_davao.getset;

import com.xpluscloud.mosesshell_davao.util.DateUtil;

/**
 * Created by Shirwen on 8/14/2017.
 */

public class Retail {

    private long id;
    private String description;
    private int ioh;
    private double price;
    private String promo;
    private String remarks;
    private String datetime;
    private String itemcode;
    private String ccode;
    private String comcode;
    private double SOS;
    private double planogram;
    private int status;

    public Retail(){
        this.description = "";
        this.ioh = 0;
        this.price = 0.0;
        this.promo = "";
        this.remarks = "";
        this.datetime = DateUtil.strDateTime(System.currentTimeMillis());
        this.itemcode = "";
        this.ccode = "";
        this.comcode = "";
        this.status = 0;
        this.SOS = 0.0;
        this.planogram = 0.0;

    }

    public double getSOS() {
        return SOS;
    }

    public void setSOS(double SOS) {
        this.SOS = SOS;
    }

    public double getPlanogram() {
        return planogram;
    }

    public void setPlanogram(double planogram) {
        this.planogram = planogram;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIoh() {
        return ioh;
    }

    public void setIoh(int ioh) {
        this.ioh = ioh;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getComcode() {
        return comcode;
    }

    public void setComcode(String comcode) {
        this.comcode = comcode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
