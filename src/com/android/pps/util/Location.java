package com.android.pps.util;

import java.io.Serializable;

import com.baidu.location.BDLocation;

public class Location implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 经度
	 */
	private double longitude;
	private float radius;

	/**
	 * 地址名称
	 */
	private String address;
	
	public String getAddress() {
		return address;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location(){}
	
	public Location(BDLocation bdLocation){
		this.address = bdLocation.getAddrStr();
		this.radius = bdLocation.getRadius();
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
