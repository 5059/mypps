package com.android.pps.util;

import java.io.Serializable;

import com.baidu.location.BDLocation;

public class Location implements Serializable{

	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * 地址名称
	 */
	private String address;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location(){}
	
	public Location(BDLocation bdLocation){
		this.latitude = bdLocation.getLatitude();
		this.longitude = bdLocation.getLongitude();
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
}
