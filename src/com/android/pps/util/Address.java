package com.android.pps.util;

import java.io.Serializable;

public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer _id;
	private String address;
	private double latitude;
	private double longitude;
	private String saveTime;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
}
