package com.xpluscloud.mosesshell_davao.getset;

import com.xpluscloud.mosesshell_davao.util.DateUtil;

/**
 * Created by Shirwen on 8/18/2017.
 */

public class Survey {
    private long id;
    private String description;
    private String frequency;
    private String source;
    private String remarks;
    private String datetime;
    private String itemcode;
    private String ccode;
    private String scode;
    private String promo;
    private int status;

    public Survey(){
        this.description = "";
        this.frequency = "";
        this.source = "";
        this.remarks = "";
        this.datetime = DateUtil.strDateTime(System.currentTimeMillis());
        this.itemcode = "";
        this.ccode = "";
        this.scode = "";
        this.promo = "";
        this.status = 0;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

