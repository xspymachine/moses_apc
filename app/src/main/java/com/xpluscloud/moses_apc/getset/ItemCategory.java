package com.xpluscloud.moses_apc.getset;

public class ItemCategory {
	private int id;	
	private String categoryCode;
	private String categoryName;
	private String description;
	private int status;
	
	public ItemCategory(){
		this.categoryCode="";
		this.categoryName="";
		this.description="";
		this.status=0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}


