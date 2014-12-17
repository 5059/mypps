package com.android.pps.db;

public class ScsanList {

	public String num;
	public String data;
	public int fail;
	public String Optype;
	public String HistoryId;
	public String Iscollect;
	public void setIscollect(String Iscollect){
		this.Iscollect = Iscollect;
	}
	public String getIscollect(){
		return Iscollect;
		
	}
	public void setHistoryId(String HistoryId){
		this.HistoryId = HistoryId;
	}
	public String getHistoryId(){
		return HistoryId;
	}
	public  void setnum(String num){
		this.num = num;
	}
	public String getnum() {
		return num;
	}
	public void setdata(String data){
		this.data = data;
	}
	public String getdata() {
		return data;
	}
	public void setfail(int fail){
		this.fail = fail;
	}
	public int getfail() {
		return fail;
		
	}
	public void setOptype(String Optype){
		this.Optype = Optype;
	}
	public String getOptype(){
		return Optype;
	}
}
