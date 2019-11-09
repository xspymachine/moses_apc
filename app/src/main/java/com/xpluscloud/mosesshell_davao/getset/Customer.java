package com.xpluscloud.mosesshell_davao.getset;

public class Customer {
	private int 	id;
	private String customerCode;
	private String name;
	private String address;
	private String brgy;
	private String city;
	private String state;
	private String contactNumber;
	private int 	acctypid;
	private int 	termid;
	private int 	typeid;
	
	private double discount;	

	private double latitude;
	private double longitude;
	private String cplanCode;
	private int buffer;
	private int cashSales;
	private int target;
	
	
	private int status;
	
		
	public Customer(){
		this.customerCode = "";
		this.name = "";
		this.address = "";
		this.brgy = "";
		this.city = "";
		this.state = "";
		this.contactNumber = "";
		this.discount=0;
		this.latitude=0;
		this.longitude=0;
		this.cplanCode = "";
		this.buffer=0;
		this.cashSales=0;
		this.target=0;
		this.status=0;
		this.typeid = 0;
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public int getTarget() {
		return target;
	}



	public void setTarget(int target) {
		this.target = target;
	}



	public String getBrgy() {
		return brgy;
	}



	public void setBrgy(String brgy) {
		this.brgy = brgy;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContactNumber() {
		return contactNumber;
	}
	
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCplanCode() {
		return cplanCode;
	}

	public void setCplanCode(String cplanCode) {
		this.cplanCode = cplanCode;
	}
	
	public int getBuffer() {
		return buffer;
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}

	public int getCashSales() {
		return cashSales;
	}

	public void setCashSales(int cashSales) {
		this.cashSales = cashSales;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getAcctypid() {
		return acctypid;
	}



	public void setAcctypid(int acctypid) {
		this.acctypid = acctypid;
	}



	public int getTermid() {
		return termid;
	}



	public void setTermid(int termid) {
		this.termid = termid;
	}

}
