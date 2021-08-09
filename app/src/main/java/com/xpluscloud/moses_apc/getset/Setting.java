package com.xpluscloud.moses_apc.getset;

public class Setting {
	
	private int id;
	private String key;
	private String value;
	private int status;
	
	public Setting(){
		this.key="";
		this.value = "";		
		this.status = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	

}
