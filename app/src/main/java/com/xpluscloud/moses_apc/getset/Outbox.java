package com.xpluscloud.moses_apc.getset;

public class Outbox {
	
	private int 	id;		
	private String dateTime;
	private String recipient;
	private String message;
	private int		priority;
	private int 	status;
	
	
	public Outbox(){				
		this.dateTime = null;
		this.recipient = "";
		this.message = "";
		this.priority=0;
		this.status = 0;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDateTime() {
		return dateTime;
	}


	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}

	
	
}
