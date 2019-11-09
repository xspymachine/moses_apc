package com.xpluscloud.mosesshell_davao.getset;

public class SearchList {
	
	private int 	id;
	private int		refid;
	private String title;
	private String description;
	private String picture;
	
	
	public SearchList(){
		this.refid=0;
		this.title="";
		this.description="";
		this.picture="";
	}


	public int getRefid() {
		return refid;
	}


	public void setRefid(int refid) {
		this.refid = refid;
	}


	public String getPicture() {
		return picture;
	}


	public void setPicture(String picture) {
		this.picture = picture;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	

	
}
