package com.xpluscloud.moses_apc.getset;

import java.sql.Date;

public class Inbox {
	
	private int 	id;		
	private Date datetime;
	private String sender;
	private String message;
	private int 	status;
	
	
	public Inbox(){				
		this.datetime = null;
		this.sender = "";
		this.message = "";
		this.status = 0;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getDatetime() {
		return datetime;
	}


	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
