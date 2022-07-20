package com.xpluscloud.moses_apc.getset;


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
	private int subcategoryid;
	private int categoryid;
	private String company;
	private String subcategory;
	private String category;
	private float priceperpack;
	private float srp;
	private String uom;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getSubcategoryid() {
		return subcategoryid;
	}

	public void setSubcategoryid(int subcategoryid) {
		this.subcategoryid = subcategoryid;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}

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
		this.subcategoryid = 0;
		this.categoryid = 0;
		this.priceperpack = 0;
		this.srp = 0;
		this.uom = "";
	}

	public float getPriceperpack() {
		return priceperpack;
	}

	public void setPriceperpack(float priceperpack) {
		this.priceperpack = priceperpack;
	}

	public float getSrp() {
		return srp;
	}

	public void setSrp(float srp) {
		this.srp = srp;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
}
