package com.xpluscloud.mosesshell_davao.getset;

public class Period {
	
	private int id;
	private int year;
	private int nweek;	
	private String week_start;
	private String week_end;
	private int status;
	
	public Period(){
		this.year=0;
		this.nweek = 0;
		this.week_start = "";
		this.week_end = "";		
		this.status = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getNweek() {
		return nweek;
	}

	public void setNweek(int nweek) {
		this.nweek = nweek;
	}

	public String getWeek_start() {
		return week_start;
	}

	public void setWeek_start(String week_start) {
		this.week_start = week_start;
	}

	public String getWeek_end() {
		return week_end;
	}

	public void setWeek_end(String week_end) {
		this.week_end = week_end;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	

}
