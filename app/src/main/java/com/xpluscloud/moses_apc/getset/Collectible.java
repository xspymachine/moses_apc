package com.xpluscloud.moses_apc.getset;

public class Collectible {
	
	private int id;
	private String date;
	private String invoiceno;
	private String ccode;
	private String customerName;
	private Double amount;
	
	
	private int status;
	
	public Collectible(){
		this.date 		= "";
		this.invoiceno	="";
		this.ccode 		= "";
		this.customerName = "";
		this.amount 	= 0.0;
		this.status 	= 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}