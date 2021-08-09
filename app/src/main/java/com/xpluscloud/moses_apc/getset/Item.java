package com.xpluscloud.moses_apc.getset;

public class Item {
	private int id;
	private String itemCode;
	private String description;
	private String categoryCode;
	private String packBarcode;
	private String barcode;
	private int qtyPerPack;

	private double pricePerPack;
	private double pricePerUnit;	
	private int priority;
	private int itemid;
	
	public Item(){
		this.itemCode = "";
		this.description = "";
		this.categoryCode="";
		this.packBarcode = "";
		this.barcode = "";
		this.qtyPerPack = 0;
		this.pricePerPack = 0;		
		this.pricePerUnit=0;
		this.itemid=0;
		this.priority=0;
	}
	
	

	public int getItemid() {
		return itemid;
	}



	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	
	public int getQtyPerPack() {
		return qtyPerPack;
	}


	public void setQtyPerPack(int qtyPerPack) {
		this.qtyPerPack = qtyPerPack;
	}



	public void setPricePerPack(double pricePerPack) {
		this.pricePerPack = pricePerPack;
	}



	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}



	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getItemCode() {
		return itemCode;
	}
	
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getCategoryCode() {
		return categoryCode;
	}
	
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	
	
	public String getPackBarcode() {
		return packBarcode;
	}
	
	public void setPackBarcode(String packBarcode) {
		this.packBarcode = packBarcode;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	
	
	public Double getPricePerPack() {
		return pricePerPack;
	}
	
	public void setPricePerPack(Double pricePerPack) {
		this.pricePerPack = pricePerPack;
	}
	
	public Double getPricePerUnit() {
		return pricePerUnit;
	}
	
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	
}
