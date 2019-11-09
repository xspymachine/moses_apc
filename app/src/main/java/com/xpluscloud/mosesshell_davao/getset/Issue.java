package com.xpluscloud.mosesshell_davao.getset;

/**
 * Created by Shirwen on 11/9/2016.
 */

public class Issue {

    private int id;
    private String code_issue;
    private String ccode;
    private String issue;
    private String date_open;
    private String date_close;
    private int status;


    public Issue(){
        this.code_issue = "";
        this.ccode = "";
        this.issue = "";
        this.date_open = ""+ System.currentTimeMillis();
        this.date_close = ""+ System.currentTimeMillis();
        this.status = 0;
    }

    public int getId() {
        return id;
    }

    public String getCode_issue() {
        return code_issue;
    }

    public void setCode_issue(String code_issue) {
        this.code_issue = code_issue;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getDate_open() {
        return date_open;
    }

    public void setDate_open(String date_open) {
        this.date_open = date_open;
    }

    public String getDate_close() {
        return date_close;
    }

    public void setDate_close(String date_close) {
        this.date_close = date_close;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
