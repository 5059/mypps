package com.android.pps;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.pps.util.Location;
import com.android.pps.util.Untilly;
import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;

public class LocationService implements Runnable{

	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";//bd09ll
	private Context context;
	private boolean mIsEngineInitSuccess = false;
	
	private LocationClient mLocationClient;
	private BDLocationListener mMyLocationListener;
	
	private Location startLoc = null;
	
	public LocationService(Context context){
		this.context = context;
		mLocationClient = new LocationClient(context.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		
		//进行一次定位
		InitLocation();
		mLocationClient.start();
		
		//在另一个线程中初始化导航引擎
        Thread locThread = new Thread(this);
        locThread.start();
	}
	
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		public void engineInitSuccess() {
			Log.i("DemoMain", "a engineInitSuccess");
			mIsEngineInitSuccess = true;
		}

		public void engineInitStart() {
			Log.i("DemoMain", "a engineInitStart");
		}

		public void engineInitFail() {
			Log.i("DemoMain", "a engineInitFail");
		}
	};
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if("4.9E-324".equals(location.getLongitude()+"")){
				Toast.makeText(LocationService.this.context,"定位失败", Toast.LENGTH_SHORT).show();
				return ;
			}
			startLoc = new Location(location);
			((MainActivity)context).setStartLoc(startLoc);
			((PPSApplication)context.getApplicationContext()).put("startLoc", startLoc);
			((MainActivity)context).dismissDialog();
			Toast.makeText(context, "定位成功", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 初始化定位参数
	 */
	private void InitLocation(){
		Log.i("MainActivity", "InitLocation");
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，如果不指定，默认返回百度坐标系.百度手机地图对外接口中的坐标系默认是bd09ll，如果配合百度地图产品的话，需要注意坐标系对应问题。 
		int span=100;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);	//设置是否要返回地址信息，默认为无地址信息。 
		mLocationClient.setLocOption(option);
	}
	
	public boolean ismIsEngineInitSuccess() {
		return mIsEngineInitSuccess;
	}

	public void setmIsEngineInitSuccess(boolean mIsEngineInitSuccess) {
		this.mIsEngineInitSuccess = mIsEngineInitSuccess;
	}

	public LocationClient getmLocationClient() {
		return mLocationClient;
	}

	public void setmLocationClient(LocationClient mLocationClient) {
		this.mLocationClient = mLocationClient;
	}

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

	@Override
	public void run() {
		 //初始化导航引擎 
        BaiduNaviManager.getInstance().initEngine((Activity)context, Untilly.getSdcardDir(),
                mNaviEngineInitListener, new LBSAuthManagerListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                    	Log.i("DemoMain", "a onAuthResult");
                        String str = null;
                        if (0 == status) {
                            str = "key校验成功!";
                        } else {
                            str = "key校验失败, " + msg;
                        }
                        Toast.makeText(LocationService.this.context, str,
                                Toast.LENGTH_LONG).show();
                    }
        });	
	}
}
