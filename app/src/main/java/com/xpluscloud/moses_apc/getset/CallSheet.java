package com.xpluscloud.moses_apc.getset;

public class CallSheet {
	
	private int id;
	private String cscode;
	private Integer sono;
	private String ccode;
	private String cusname;
	private String date;
	private int buffer;
	private int cash_sales;
	private double payment;
	private String supplier;
	private int termid;
	
	private int status;
	
	public CallSheet(){
		this.cscode		= "";
		this.sono		= 0;
		this.ccode 		= "";
		this.cusname 	= "";
		this.date 		= "";
		this.buffer 	= 0;
		this.cash_sales = 0;
		this.payment 	= 0.0;
		this.supplier 	= "";
		this.status 	= 0;
		this.termid 	= 0;
	}

	public int getTermid() {
		return termid;
	}

	public void setTermid(int termid) {
		this.termid = termid;
	}

	public String getSupplier() {
		return supplier;
	}



	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}



	public double getPayment() {
		return payment;
	}



	public void setPayment(double payment) {
		this.payment = payment;
	}



	public String getcusname() {
		return cusname;
	}



	public void setcusname(String cusname) {
		this.cusname = cusname;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCscode() {
		return cscode;
	}

	public void setCscode(String cscode) {
		this.cscode = cscode;
	}
	
	public Integer getSono() {
		return sono;
	}

	public void setSono(Integer sono) {
		this.sono = sono;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getBuffer() {
		return buffer;
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}

	public int getCash_sales() {
		return cash_sales;
	}

	public void setCash_sales(int cash_sales) {
		this.cash_sales = cash_sales;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
