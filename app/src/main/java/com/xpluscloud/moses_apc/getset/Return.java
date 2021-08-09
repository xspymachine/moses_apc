package com.xpluscloud.moses_apc.getset;

public class Return {
	
	private int id;
	private String date;
	private String prcode;
	private int prno;
	private String ccode;
	private String customerName;
	private String itemcode;
	private String itemDescription;
	private String pckg;
	private int qty;	
	private double price;
	private double amount;
	
	
	private int status;
	
	public Return(){
		this.date = "";
		this.prcode="";
		this.prno=0;
		this.ccode = "";
		this.customerName = "";
		this.itemcode = "";
		this.itemDescription="";
		this.pckg = "";
		this.price = 0.0;
		this.qty = 0;
		this.amount=0;
		this.status = 0;
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
	
	public String getPrcode() {
		return prcode;
	}

	public void setPrcode(String prcode) {
		this.prcode = prcode;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPrno() {
		return prno;
	}

	public void setPrno(int prno) {
		this.prno = prno;
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

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getPckg() {
		return pckg;
	}

	public void setPckg(String pckg) {
		this.pckg = pckg;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
}
