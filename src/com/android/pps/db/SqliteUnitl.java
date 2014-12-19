package com.android.pps.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.pps.util.Address;

public class SqliteUnitl {

	/**
	 * 插入地址数据
	 */
	public static void insertAddress(Context context, Address address){
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put("address", address.getAddress());
		values.put("latitude", address.getLatitude());
		values.put("longitude", address.getLongitude());
		values.put("saveTime", address.getSaveTime());
		database.insert("addressInfo", null, values );
		Log.e("text", "插入数据");
	}
	
	//收藏列表插入数据
	public static void CollectInsert(Context context , String data,String Num , int fail ,String Optype){
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("CollectNum", Num);
		values.put("Data", data);
		values.put("Fail", fail);
		values.put("Optype", Optype);
		database.insert("collect", null, values );
	}
	
	/**
	 * 获取所有address数据
	 * @param context
	 * @return
	 */
	public static List<Address> getAllAddress(Context context){
		List<Address> addreList = new ArrayList<Address>();
		DBOpenHelper helper = new DBOpenHelper(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query("addressInfo", new String[]{"_id", "address", "latitude", "longitude", "saveTime"}, null, null, null, null, "_id asc");
		while(cursor.moveToNext()){
			Address address = new Address();
			address.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			address.setAddress(cursor.getString(cursor.getColumnIndex("address")));
			address.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
			address.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
			address.setSaveTime(cursor.getString(cursor.getColumnIndex("saveTime")));
			addreList.add(address);
		}
		return addreList;
	}

	/**
	 * 删除指定id的address
	 * @param onClickListener
	 * @param get_id
	 */
	public static void deleteAddress(Context context, Integer _id) {
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		database.delete("addressInfo", "_id = ?", new String[]{_id+""});
	}

	/**
	 * 判断该address是否存在
	 * @param addTartgetActivity
	 * @param addrStr
	 * @return
	 */
	public static boolean isExistedAddress(Context context, String addrStr) {
		DBOpenHelper helper = new DBOpenHelper(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query("addressInfo", new String[]{"_id"}, "address=?", new String[]{addrStr}, null, null, null);
		if(cursor.getCount() == 0){
			return false;	//不存在
		}
		return true;	//已经存在
	}

}
