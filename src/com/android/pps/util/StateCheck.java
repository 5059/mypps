package com.android.pps.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

public class StateCheck {

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPenOneNetwork(final Context context) {
		boolean gps = isOpenGPS(context);	//是否打开GPS
		boolean network = isNetworkAvailable(context);	//wife连接到热点即为true,手机网络连接为true
		boolean wife = isWifiEnable(context);	//wife功能打开即为true,无论是否连接热点
		boolean con = isNetworkConnected(context);  //wife连接到热点即为true,手机网络连接为true
		
		
		Toast.makeText(context, gps + " " + network + " " + wife + " " + con, Toast.LENGTH_SHORT).show();
		if (gps || network || wife) {
			return true;
		}

		return false;
	}

	public static boolean isOpenGPS(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//		boolean networkLoc = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);	//gps打开即为true
		return gps;
	}

	public static boolean isNetworkConnected(final Context context) {
		ConnectivityManager cm =   
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo networkk = cm.getActiveNetworkInfo();  
        if (networkk != null) {  
            return networkk.isConnected();
        }
		return false;
	}
	
    // 是否有可用网络  
    public static boolean isNetworkAvailable(final Context context) {  
        ConnectivityManager cm =   
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo network = cm.getActiveNetworkInfo();  
        if (network != null) {  
            return network.isAvailable();
        }  
        return false;  
    }
    
    // Wifi是否可用  
    public static boolean isWifiEnable(final Context context) {  
        WifiManager wifiManager = (WifiManager) context  
                .getSystemService(Context.WIFI_SERVICE);  
        return wifiManager.isWifiEnabled();  
    }  
	

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);

		} catch (ActivityNotFoundException ex) {
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				context.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

}
