package com.xpluscloud.mosesshell_davao.getset;

/**
 * Created by Shirwen on 10/28/2016.
 */

public class CallChecklist {

    private int _id;
    private int chkid;
    private int catid;
    private String description;
    private String category;
    private int status;

    public CallChecklist(){
        this.description = "";
        this.category = "";
        this.status = 0;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getChkid() {
        return chkid;
    }

    public void setChkid(int chkid) {
        this.chkid = chkid;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
