package com.xpluscloud.mosesshell_davao.getset;

public class Signature {

	private int 	id;	
	private String customerCode;
	private String datetime;
	private String filename;
	private int 	status;
	
	
	public Signature(){		
		this.customerCode = "";
		this.datetime = null;
		this.filename = "";		
		this.status = 0;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public String getDatetime() {
		return datetime;
	}


	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
}
