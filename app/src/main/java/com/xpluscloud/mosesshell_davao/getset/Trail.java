package com.xpluscloud.mosesshell_davao.getset;

import java.sql.Date;

public class Trail {
	
	private int 	id;			
	private Date gpstime;
	private float 	latitude; 
	private float 	longitude;
	private float 	accuracy;
	private float 	speed;
	private float 	bearing;
	private float 	provider;
	private int 	status;
	
	
	public Trail(){
		this.gpstime = null;
		this.latitude = 0;
		this.longitude = 0;
		this.accuracy = 0;
		this.speed = 0;
		this.bearing = 0;
		this.provider = 0;
		this.status = 0;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getGpstime() {
		return gpstime;
	}


	public void setGpstime(Date gpstime) {
		this.gpstime = gpstime;
	}


	public float getLatitude() {
		return latitude;
	}


	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}


	public float getLongitude() {
		return longitude;
	}


	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}


	public float getAccuracy() {
		return accuracy;
	}


	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}


	public float getSpeed() {
		return speed;
	}


	public void setSpeed(float speed) {
		this.speed = speed;
	}


	public float getBearing() {
		return bearing;
	}


	public void setBearing(float bearing) {
		this.bearing = bearing;
	}


	public float getProvider() {
		return provider;
	}


	public void setProvider(float provider) {
		this.provider = provider;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
