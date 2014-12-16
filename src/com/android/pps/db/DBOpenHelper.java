package com.android.pps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper{

	//数据库版本  
    private static final int VERSION = 1;
    
    //数据库名称
    private static final String dbName = "pps.db";
    //新建一个表  
    String sql = "create table if not exists addressInfo"+  
    "(_id integer primary key autoincrement,address varchar,latitude double,longitude double,saveTime varchar)";  
	
	public DBOpenHelper(Context context) {
		super(context, dbName, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
