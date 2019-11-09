package com.xpluscloud.mosesshell_davao.getset;

public class TimeInOut {

	private int 	id;	
	private String customerCode;
	private String datetime;
	private int 	inout;
	private double 	latitude; 
	private double 	longitude; 		
	private int 	status;
	
	
	public TimeInOut(){		
		this.customerCode = "";
		this.datetime = null;
		this.inout = 0;
		this.latitude = 0;
		this.longitude = 0;
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


	public String getDateTime() {
		return datetime;
	}


	public void setDateTime(String datetime) {
		this.datetime = datetime;
	}


	public int getInout() {
		return inout;
	}


	public void setInout(int inout) {
		this.inout = inout;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double lat) {
		this.latitude = lat;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
}
