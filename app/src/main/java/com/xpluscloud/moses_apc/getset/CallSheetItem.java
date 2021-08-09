package com.xpluscloud.moses_apc.getset;

public class CallSheetItem {
	
	private int id;
	private String cscode;
	private String itemcode;
	private String category;
	private String description;
	private String pckg;
	private int pastinvt;
	private int delivery;
	private int presentinvt;
	private int offtake;
	private int ico;
	private int suggested;
	private int order;
	private double price;
	private String delivery_date;
	private double sharepts;
	private double liters;
	private int totalSharePts;
	
	private int status;

	public CallSheetItem(){
		this.cscode		= "";
		this.itemcode 	= "";
		this.category	= "";
		this.description= "";
		this.pckg 		= "";
		this.pastinvt 	= 0;
		this.delivery 	= 0;
		this.presentinvt = 0;
		this.offtake 	= 0;
		this.ico 		= 0;
		this.suggested 	= 0;
		this.order 		= 0;
		this.price		= 0.0;
		this.status 	= 0;
		this.delivery_date = "";
		this.sharepts = 0.0;
		this.liters = 0.0;
		this.totalSharePts = 0;
	}
	public int getTotalSharePts() {
		return totalSharePts;
	}

	public void setTotalSharePts(int totalSharePts) {
		this.totalSharePts = totalSharePts;
	}

	public double getLiters() {
		return liters;
	}

	public void setLiters(double liters) {
		this.liters = liters;
	}

	public double getSharepts() {
		return sharepts;
	}

	public void setSharepts(double sharepts) {
		this.sharepts = sharepts;
	}

	public String getDelivery_date() {
		return delivery_date;
	}

	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getPckg() {
		return pckg;
	}

	public void setPckg(String pckg) {
		this.pckg = pckg;
	}

	public int getPastinvt() {
		return pastinvt;
	}

	public void setPastinvt(int pastinvt) {
		this.pastinvt = pastinvt;
	}

	public int getDelivery() {
		return delivery;
	}

	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}

	public int getPresentinvt() {
		return presentinvt;
	}

	public void setPresentinvt(int presentinvt) {
		this.presentinvt = presentinvt;
	}

	public int getOfftake() {
		return offtake;
	}

	public void setOfftake(int offtake) {
		this.offtake = offtake;
	}

	public int getIco() {
		return ico;
	}

	public void setIco(int ico) {
		this.ico = ico;
	}

	public int getSuggested() {
		return suggested;
	}

	public void setSuggested(int suggested) {
		this.suggested = suggested;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
