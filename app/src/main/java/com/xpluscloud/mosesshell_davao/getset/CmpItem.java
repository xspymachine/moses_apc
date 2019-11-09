package com.xpluscloud.mosesshell_davao.getset;

public class CmpItem {
	private int id;
	private String itemCode;
	private String description;
	private String categoryCode;
	private String packBarcode;
	private String barcode;
	private int status;
	private String case_barcode;
	private int groupid;
	private int category_id;
	private int subcategory_id;

	public CmpItem(){
		this.itemCode = "";
		this.description = "";
		this.categoryCode="";
		this.packBarcode = "";
		this.barcode = "";
		this.status = 0;
		this.case_barcode = "";
		this.groupid = 0;
		this.category_id = 0;
		this.subcategory_id = 0;
	}

	public String getCase_barcode() {
		return case_barcode;
	}

	public void setCase_barcode(String case_barcode) {
		this.case_barcode = case_barcode;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getSubcategory_id() {
		return subcategory_id;
	}

	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
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



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
