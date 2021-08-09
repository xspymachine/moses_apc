package com.xpluscloud.moses_apc.getset;

public class Pullout {
	
	private int id;
	private String date;
	private String sono;
	private String ccode;
	private String itemcode;
	private String pckg;
	private int qty;	
	private Double price;
	
	private int status;
	
	public Pullout(){
		this.date = "";
		this.sono="";
		this.ccode = "";
		this.itemcode = "";
		this.pckg = "";
		this.price = 0.0;
		this.qty = 0;
		this.status = 0;
	}

	public String getPckg() {
		return pckg;
	}

	public void setPckg(String pckg) {
		this.pckg = pckg;
	}

	public String getSono() {
		return sono;
	}

	public void setSono(String sono) {
		this.sono = sono;
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


	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}	

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
