package com.xpluscloud.moses_apc.getset;

public class CoveragePlan {
	
	private int id;
	private int cpid;
	private String cplanCode;
	private String description;
	private String weekSchedule;
	private String daySchedule;
	private int status;
	
	public CoveragePlan(){
		this.cpid=0;
		this.cplanCode = "";
		this.description = "";
		this.weekSchedule = "";
		this.daySchedule = "";
		this.status = 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCpId() {
		return cpid;
	}
	public void setCpId(int cpid) {
		this.cpid = cpid;
	}
	public String getCplanCode() {
		return cplanCode;
	}
	public void setCplanCode(String cplanCode) {
		this.cplanCode = cplanCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWeekSchedule() {
		return weekSchedule;
	}
	public void setWeekSchedule(String weekSchedule) {
		this.weekSchedule = weekSchedule;
	}
	public String getDaySchedule() {
		return daySchedule;
	}
	public void setDaySchedule(String daySchedule) {
		this.daySchedule = daySchedule;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	

}
