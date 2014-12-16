package com.android.pps.navi;

import com.android.pps.*;
import com.android.pps.baidu.RoutePlanDemo;
import com.android.pps.util.Location;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditDesActivity extends ActionBarActivity {
	
	private EditText tv_addr, tv_lati, tv_long; 
	private ImageButton imgBtn_toNavi;

	private Location startLoc, endLoc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_imgresult);
		
		//获取起点位置信息
		startLoc = (Location) ((PPSApplication)getApplicationContext()).get("startLoc");
		
		tv_addr = (EditText)findViewById(R.id.tv_addrValue);
		tv_lati = (EditText)findViewById(R.id.tv_latiValue);
		tv_long = (EditText)findViewById(R.id.tv_longValue);
		imgBtn_toNavi = (ImageButton)findViewById(R.id.btn_navi);
		imgBtn_toNavi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tv_addr.getText().equals("") || tv_lati.getText().equals("") || tv_long.getText().equals("")){
					Toast.makeText(EditDesActivity.this, "请输入目的地位置信息", Toast.LENGTH_SHORT).show();
				}else{
					endLoc = new Location();
					endLoc.setAddress(tv_addr.getText().toString());
					endLoc.setLongitude(Double.parseDouble(tv_long.getText().toString()));
					endLoc.setLatitude(Double.parseDouble(tv_lati.getText().toString()));
					//已经定位才能去规划路径
					if(startLoc != null){
						Intent intent = new Intent(EditDesActivity.this, RoutePlanDemo.class);
						//将起终点信息传给导航activity
						intent.putExtra("startLocation", startLoc);
						intent.putExtra("endLocation", endLoc);
						startActivity(intent);
					}else{
						if(startLoc == null){
							Toast.makeText(EditDesActivity.this, "请先进行定位获取起点地址！", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
