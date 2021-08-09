package com.xpluscloud.moses_apc.getset;

public class Pr {
	
	private int id;
	private String prcode;
	private String date;
	private String ccode;
	private int prno;
	private Double amount;
	private int status;
	
	public Pr(){
		this.prcode="";
		this.ccode="";
		this.date = "";
		this.prno=0;
		this.amount = 0.0;
		this.status = 0;
	}

	
	
	public String getCcode() {
		return ccode;
	}



	public void setCcode(String ccode) {
		this.ccode = ccode;
	}



	public String getPrcode() {
		return prcode;
	}

	public void setPrcode(String prcode) {
		this.prcode = prcode;
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

	public int getPrno() {
		return prno;
	}

	public void setPrno(int prno) {
		this.prno = prno;
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
