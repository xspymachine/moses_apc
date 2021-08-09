package com.xpluscloud.moses_apc.getset;

public class SalesCall {

	private int 	id;	
	private String datetime;
	private String customerCode;
	private String blob;
	private int 	status;
	
	public SalesCall(){		
		this.customerCode = "";
		this.datetime = null;
		this.blob = "";
		this.status = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getBlob() {
		return blob;
	}

	public void setBlob(String blob) {
		this.blob = blob;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
