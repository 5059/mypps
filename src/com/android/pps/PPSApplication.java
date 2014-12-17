package com.android.pps;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

public class PPSApplication extends Application{

	private static HashMap<String, Object> map;
	static{
		 map = new HashMap<String, Object>();
	}
	
	public PPSApplication(){
		Log.e("text", "者发放空间");
	}
	
	
	public HashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}
	
	public void put(String key, Object value){
		map.put(key, value);
	}
	
	public Object get(String key){
		return map.get(key);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
	}
}
