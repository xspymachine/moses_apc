package com.xpluscloud.moses_apc.getset;

public class Truck {
	
	private int id;
	private String ccode;
	private int truckid;
	private String name;
	private String capacity;
	private String type;
	private int quantity;
	private int status;
	
	public Truck(){
		
		this.ccode		= "";
		this.truckid	= 0;
		this.name 		= "";
		this.capacity 	= "";
		this.type 		= "";
		this.quantity 	= 0;
		this.status 	= 0;
		
	}	

	public int getTruckid() {
		return truckid;
	}

	public void setTruckid(int truckid) {
		this.truckid = truckid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

}
