package com.xpluscloud.moses_apc.getset;

/**
 * Created by Shirwen on 10/28/2016.
 */

public class CustomerCallChecklist {

    private int _id;
    private String ccode;
    private int chkid;
    private int chkYes;
    private int chkNo;
    private String gremarks;
    private String date;
    private String time;
    private int status;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public int getChkid() {
        return chkid;
    }

    public void setChkid(int chkid) {
        this.chkid = chkid;
    }

    public String getGremarks() {
        return gremarks;
    }

    public void setGremarks(String gremarks) {
        this.gremarks = gremarks;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChkNo() {
        return chkNo;
    }

    public void setChkNo(int chkNo) {
        this.chkNo = chkNo;
    }

    public int getChkYes() {
        return chkYes;
    }

    public void setChkYes(int chkYes) {
        this.chkYes = chkYes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CustomerCallChecklist(){
        this.ccode = "";
        this.chkid = 0;
        this.chkYes = 0;

        this.chkNo = 0;
        this.gremarks = "";
        this.date = "";
        this.time = "";
        this.description = "";
        this.status = 0;
    }
}
