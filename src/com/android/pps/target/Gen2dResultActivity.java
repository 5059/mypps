package com.android.pps.target;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.pps.MainActivity;
import com.android.pps.R;
import com.android.pps.util.QRCodeService;
import com.android.pps.util.QRToSDcardSaveService;
import com.android.pps.util.Untilly;

public class Gen2dResultActivity extends ActionBarActivity {

	private ImageView imgV_gen2d;
	private ImageButton imgBtn_save, imgBtn_send;

	/**
	 * 地址名称, 格式化的地址信息
	 */
	private String formatAddressInfo;
	/**
	 * 标识是否已经存储该地址
	 */
	private boolean isSavedImg = false;
	/**
	 * 图像的路径
	 */
	private String imgPath = null, dateStamp;
	
	private Bitmap bitmap = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_2d);
		isSavedImg = getIntent().getBooleanExtra("isSaved2DImg", false);
		imgPath = getIntent().getStringExtra("imgPath");
        // 从savedInstanceState中恢复数据, 如果没有数据需要恢复savedInstanceState为null  
        if (savedInstanceState != null) {  
            isSavedImg = savedInstanceState.getBoolean("isSavedImg");  
            imgPath = savedInstanceState.getString("imgPath");  
        }  
        
		imgV_gen2d = (ImageView)findViewById(R.id.iv_2d);
		imgBtn_save = (ImageButton)findViewById(R.id.bn_save);
		imgBtn_send = (ImageButton)findViewById(R.id.bn_send);
		
		Intent intent = getIntent();
		formatAddressInfo = intent.getStringExtra("formatAddressInfo");
		dateStamp = intent.getStringExtra("dateStamp");
		
		//先生成二维码
		bitmap = QRCodeService.CreateQRCode(formatAddressInfo, null);
		imgV_gen2d.setImageBitmap(bitmap);
		
		if(!isSavedImg){
			//保存到缓存文件中
			QRToSDcardSaveService saveService = new QRToSDcardSaveService();
			imgPath = saveService.saveToSDCard(MainActivity.PATH_CACHE, dateStamp + ".jpg", bitmap);
		}
		
		imgBtn_send.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v){
				 //发送二维码
				 Untilly.shareImg(Gen2dResultActivity.this, "发送位置二维码", imgPath);
			 }
		});

		imgBtn_save.setOnClickListener(new OnClickListener(){
			 @Override
			 public void onClick(View v){
				 //保存二维码
				 if(!isSavedImg) {
					 //保存到文件中
					 QRToSDcardSaveService saveService = new QRToSDcardSaveService();
					 imgPath = saveService.saveToSDCard(MainActivity.PATH_IMG, dateStamp + ".jpg", bitmap);
					 //删除缓存图片
					 Untilly.deleteFile(imgPath.replace("image", "cache"));
					 Log.i("IMGGGGG",  isSavedImg +" ddd");
					 isSavedImg = true;
					 Intent resultIntent = new Intent();
					 resultIntent.putExtra("isSaved2DImg", isSavedImg);
					 resultIntent.putExtra("imgPath", imgPath);
					 Gen2dResultActivity.this.setResult(RESULT_OK, resultIntent);
					 Toast t = Toast.makeText(Gen2dResultActivity.this, "二维码已保存到:"+imgPath, Toast.LENGTH_LONG);
					 t.setDuration(5000);
					 t.show();
				 }else{
					 Toast.makeText(Gen2dResultActivity.this, "请勿重复保存", Toast.LENGTH_SHORT).show();
				 }
			 }
		});
		
	}

	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
		 super.onSaveInstanceState(outState);
		 //存储当前状态
		 outState.putBoolean("isSavedImg", isSavedImg);
		 outState.putString("imgPath", imgPath);
	 }

	 @Override
	 protected void onRestoreInstanceState(Bundle savedState) {
		 super.onRestoreInstanceState(savedState);
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
