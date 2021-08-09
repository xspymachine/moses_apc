package com.xpluscloud.moses_apc.getset;

public class MyList {
	
	private int 	id;		
	private String dateTime;
	private String customerCode;
	private String customerName;
	private String address;
	private String transaction;
	private String transCode;


	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public MyList(){
		this.dateTime = null;
		this.customerCode = "";
		this.customerName = "";
		this.address="";

		this.transaction="";
		this.transCode = "";
	}
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTransaction() {
		return transaction;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
}

