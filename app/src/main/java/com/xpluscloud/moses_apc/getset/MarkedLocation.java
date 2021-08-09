package com.xpluscloud.moses_apc.getset;

public class MarkedLocation {

	private int 	id;	
	private String customerCode;
	private String gpstime;
	private double 	latitude; 
	private double 	longitude;
	private float 	accuracy;
	private String provider;
	private int 	status;
	
	
	public MarkedLocation(){		
		this.customerCode = "";
		this.gpstime = null;
		this.latitude = 0;
		this.longitude = 0;
		this.accuracy = 0;
		this.provider = "";
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


	public String getGpstime() {
		return gpstime;
	}


	public void setGpstime(String gpstime) {
		this.gpstime = gpstime;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double d) {
		this.latitude = d;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double d) {
		this.longitude = d;
	}


	public float getAccuracy() {
		return accuracy;
	}


	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}


	public String getProvider() {
		return provider;
	}


	public void setProvider(String provider) {
		this.provider = provider;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
