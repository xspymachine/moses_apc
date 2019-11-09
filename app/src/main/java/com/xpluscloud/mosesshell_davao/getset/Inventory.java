package com.xpluscloud.mosesshell_davao.getset;


public class Inventory {

	private int 	id;
	private String INCode;
	private Integer INno;
	private String date;
	private String ccode;
	private String itemCode;
	private String pckg;
	private int 	qty;
	private float 	price; 	
	private int 	status;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getINCode() {
		return INCode;
	}

	public void setINCode(String INCode) {
		this.INCode = INCode;
	}

	public Integer getINno() {
		return INno;
	}

	public void setINno(Integer INno) {
		this.INno = INno;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Inventory(){
		this.ccode 		= "";
		this.date 		= "";
		this.itemCode 	= "";
		this.pckg 		= "";
		this.qty 		= 0;
		this.price 		= 0;
		this.status 	= 0;
		this.description = "";
	}
	
	
	
	
	
}
