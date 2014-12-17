package com.android.pps.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteUnitl {

	/**
	 * 插入地址数据
	 */
	public static void insertAddress(Context context, String address, double latitude, double longitude, String saveTime){
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues(); 
		values.put("address", address);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		values.put("saveTime", saveTime);
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
	
	public static void HistoryUpdate(Context context,String Iscollect,String HistoryId,String collectdate){
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Iscollect", Iscollect);
		values.put("collectDate",collectdate);
		database.update("history", values , "HistoryId = ?", new String[]{HistoryId});
	}
	
	//根据id删除数据
	public static void HistoryDelect(Context context, String HistoryId){
		DBOpenHelper mOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = mOpenHelper.getWritableDatabase();
		database.delete("history", "HistoryId = ?", new String[]{HistoryId});
	}
	
	public static void history(Context context){
		List<ScsanList> mlist = new ArrayList<ScsanList>();
		DBOpenHelper helper = new DBOpenHelper(context);
		SQLiteDatabase database = helper.getReadableDatabase();
		Cursor cursor = database.query("history", new String[]{"HistoryNum","Fail","Data","Optype","Iscollect"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			String Optype = cursor.getString(cursor.getColumnIndex("Optype"));
			if(Optype.equals("生成")){
				String Iscollect = cursor.getString(cursor.getColumnIndex("Iscollect"));
				if(Iscollect.equals("是")){
				ScsanList list = new  ScsanList();
				String num = cursor.getString(cursor.getColumnIndex("HistoryNum"));
				int fail = cursor.getInt(cursor.getColumnIndex("Fail"));
				String data = cursor.getString(cursor.getColumnIndex("Data"));
				list.setdata(data);
				list.setfail(fail);
				list.setnum(num);
				list.setIscollect(Iscollect);
				mlist.add(list);
				}
			}
		}
	}
}
