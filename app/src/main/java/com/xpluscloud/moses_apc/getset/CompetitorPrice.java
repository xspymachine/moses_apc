package com.xpluscloud.moses_apc.getset;

public class CompetitorPrice {
	
	private int id;
	private String comcode;
	private String ccode;
	private String icode;
	private String description;
	private String catcode;
	private String dtrPrice;
	private String retailPrice;
	private int qty;
	private int vol;
	private int ioh;
	private int status;
	private String datetime;
	
	private int type50;
	private int type22;
	private int type5;
	private int type7;
	
	private String type50ioh;
//	private int type50ioh;
	private int type22ioh;
	private int type5ioh;
	private int type7ioh;
	
	private String remarks;

	public CompetitorPrice(){
		
		this.comcode	 = "";
		this.ccode		 = "";
		this.icode 		 = "";
		this.description = "";
		this.catcode	 = "";
		this.dtrPrice	 = "0.00";
		this.retailPrice = "0.00";
		this.qty		 = 0;
		this.vol		 = 0;
		this.status		 = 0;
		this.ioh	     = 0;
		
		this.type50		 = 0;
		this.type22		 = 0;
		this.type5		 = 0;
		this.type7		 = 0;
		
		this.type50ioh	 = "0.00";
		this.type22ioh	 = 0;
		this.type5ioh	 = 0;
		this.type7ioh	 = 0;
		
		this.remarks 	 = "";
		this.datetime	 = "";
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getType50ioh() {
		return type50ioh;
	}

	public void setType50ioh(String type50ioh) {
		this.type50ioh = type50ioh;
	}

	public int getType22ioh() {
		return type22ioh;
	}

	public void setType22ioh(int type22ioh) {
		this.type22ioh = type22ioh;
	}

	public int getType5ioh() {
		return type5ioh;
	}

	public void setType5ioh(int type5ioh) {
		this.type5ioh = type5ioh;
	}

	public int getType7ioh() {
		return type7ioh;
	}

	public void setType7ioh(int type7ioh) {
		this.type7ioh = type7ioh;
	}

	public int getType50() {
		return type50;
	}

	public void setType50(int type50) {
		this.type50 = type50;
	}

	public int getType22() {
		return type22;
	}

	public void setType22(int type22) {
		this.type22 = type22;
	}

	public int getType5() {
		return type5;
	}

	public void setType5(int type5) {
		this.type5 = type5;
	}

	public int getType7() {
		return type7;
	}

	public void setType7(int type7) {
		this.type7 = type7;
	}

	public int getIoh() {
		return ioh;
	}

	public void setIoh(int ioh) {
		this.ioh = ioh;
	}

	public String getDtrPrice() {
		return dtrPrice;
	}

	public void setDtrPrice(String dtrPrice) {
		this.dtrPrice = dtrPrice;
	}

	public String getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}

	public int getVol() {
		return vol;
	}

	public void setVol(int vol) {
		this.vol = vol;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComcode() {
		return comcode;
	}

	public void setComcode(String comcode) {
		this.comcode = comcode;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getIcode() {
		return icode;
	}

	public void setIcode(String icode) {
		this.icode = icode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatcode() {
		return catcode;
	}

	public void setCatcode(String catcode) {
		this.catcode = catcode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
