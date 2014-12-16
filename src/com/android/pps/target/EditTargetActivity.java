package com.android.pps.target;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pps.R;
import com.android.pps.db.SqliteUnitl;
import com.android.pps.util.Location;
import com.android.pps.util.Untilly;

public class EditTargetActivity extends ActionBarActivity {
	
	private ImageButton imgBtn_send, imgBtn_gen2d, imgBtn_save;
	private TextView textV_lng, textV_lat, textV_addr;
	private int GEN2DRESULT = 0;
	/**
	 * 地址名称, 格式化的地址信息
	 */
	private String address, formatAddressInfo;
	/**
	 * 地址对象
	 */
	private Location startLocation;
	/**
	 * 标识是否已经存储该地址
	 */
	private boolean isSavedImg = false;
	/**
	 * 标识Gen2dResultActivity是否已经存储该二维码
	 */
	private boolean isSaved2DImg = false;
	
	/**
	 * Gen2dResultActivity存储图像的路径
	 */
	private String imgPath = null, dateStamp;
	
	public EditTargetActivity(){
		Log.i("凤飞飞凤飞飞", "construction");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_addinfo);
		Log.i("凤飞飞凤飞飞", "onCreate");
        // 从savedInstanceState中恢复数据, 如果没有数据需要恢复savedInstanceState为null  
        if (savedInstanceState != null) {  
            isSavedImg = savedInstanceState.getBoolean("isSavedImg");  
            imgPath = savedInstanceState.getString("imgPath");
            dateStamp = savedInstanceState.getString("dateStamp");  
        }  
		
		imgBtn_send = (ImageButton) findViewById(R.id.bn_send);
		imgBtn_gen2d = (ImageButton) findViewById(R.id.bn_gen);
		imgBtn_save = (ImageButton) findViewById(R.id.bn_save);
		textV_lng = (TextView) findViewById(R.id.tv_lng);
		textV_lat = (TextView) findViewById(R.id.tv_lat);
		textV_addr = (TextView) findViewById(R.id.tv_add);
		
		Intent  intent =getIntent();
		Bundle bundle = intent.getExtras();
		address = bundle.getString("address");
		dateStamp = Untilly.getFormateDateStamp();
		startLocation = (Location) bundle.getSerializable("startLocation");
		formatAddressInfo = getFormatAddressInfo();
		
		textV_addr.setText(address);
		textV_lat.setText(startLocation.getLatitude() + "");
		textV_lng.setText(startLocation.getLongitude() + "");
		
		imgBtn_gen2d.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v){
				 Intent intent = new Intent(EditTargetActivity.this, Gen2dResultActivity.class);
				 intent.putExtra("formatAddressInfo", formatAddressInfo);
				 intent.putExtra("dateStamp", dateStamp);
				 intent.putExtra("isSaved2DImg", isSaved2DImg);
				 intent.putExtra("imgPath", imgPath);
				 startActivityForResult(intent, GEN2DRESULT);
			 }
		});
		
		imgBtn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("IMGGGGG",  isSavedImg +"");
				if(!isSavedImg) {
					//保存到数据库
					SqliteUnitl.insertAddress(EditTargetActivity.this, address, startLocation.getLatitude(), startLocation.getLongitude(), dateStamp);
					isSavedImg = true;
					Toast.makeText(EditTargetActivity.this, "当前位置信息已保存", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(EditTargetActivity.this, "请勿重复保存", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		imgBtn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//分享位置信息
				Untilly.shareText(EditTargetActivity.this, "发送位置信息", formatAddressInfo);
			}
		});
	}

	/**
	 * 获取格式化的地址信息
	 * @return
	 */
	private String getFormatAddressInfo(){
		StringBuffer sb = new StringBuffer(100);
		sb.append("地址：").append(address).append("\n");
		sb.append("经度：").append(startLocation.getLongitude()).append("\n");
		sb.append("纬度：").append(startLocation.getLatitude());
		return sb.toString();
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
	
	@Override
	protected void onRestart() {
		Log.i("凤飞飞凤飞飞", "onRestart");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		Log.i("凤飞飞凤飞飞", "onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
//		Editor sharedata = getSharedPreferences("data", 0).edit();    
//		sharedata.putBoolean("isSavedImg", isSavedImg);    
//		sharedata.commit();  
		Log.i("凤飞飞凤飞飞", "onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Log.i("凤飞飞凤飞飞", "onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.i("凤飞飞凤飞飞", "onDestroy");
		super.onDestroy();
	}
	
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
		 super.onSaveInstanceState(outState);
		 //存储当前状态
		 outState.putBoolean("isSavedImg", isSavedImg);
		 outState.putString("imgPath", imgPath);
		 outState.putString("dateStamp", dateStamp);
	 }

	 @Override
	 protected void onRestoreInstanceState(Bundle savedState) {
		 super.onRestoreInstanceState(savedState);
	 }
	 
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 if(resultCode == RESULT_OK){
	        	if(requestCode == GEN2DRESULT){
	        		isSaved2DImg = data.getBooleanExtra("isSaved2DImg", false);
	        		imgPath = data.getStringExtra("imgPath");
	        	}
	     }
	}
}
