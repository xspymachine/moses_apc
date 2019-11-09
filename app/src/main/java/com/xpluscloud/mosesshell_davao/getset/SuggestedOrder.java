package com.xpluscloud.mosesshell_davao.getset;

public class SuggestedOrder {
	
	private String customerCode;
	private String itemCode;
	private double 	suggestedPack;
	private double 	suggestedUnit;
	
	
	public SuggestedOrder(){				
		
		this.customerCode = "";
		this.itemCode = "";
		this.suggestedPack=0;	
		this.suggestedUnit=0;
	}


	public String getCustomerCode() {
		return customerCode;
	}


	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}


	public String getItemCode() {
		return itemCode;
	}


	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}


	public double getSuggestedPack() {
		return suggestedPack;
	}


	public void setSuggestedPack(double suggestedPack) {
		this.suggestedPack = suggestedPack;
	}


	public double getSuggestedUnit() {
		return suggestedUnit;
	}


	public void setSuggestedUnit(double suggestedUnit) {
		this.suggestedUnit = suggestedUnit;
	}
	
	
}
