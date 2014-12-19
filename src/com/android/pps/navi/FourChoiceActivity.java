package com.android.pps.navi;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.pps.PPSApplication;
import com.android.pps.R;
import com.android.pps.baidu.RoutePlanDemo;
import com.android.pps.target.ChooseAddrActivity;
import com.android.pps.util.Address;
import com.android.pps.util.Location;
import com.android.pps.util.Untilly;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zxing.view.MipcaActivityCapture;

public class FourChoiceActivity extends ActionBarActivity {

	private final static int SCAN_REQUEST_CODE = 1;
	private final static int CHOOSEIMG_REQUEST_CODE = 2;
	private final static int CHOOSE_ADDRESS_CODE = 3;
	
	private ImageButton imgBtn_choice2d, imgBtn_chooseAddr, imgBtn_scan2d, imgBtn_editAddr;
	
	private Location startLoc = null, endLoc = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_fourchoice);
		imgBtn_choice2d = (ImageButton) findViewById(R.id.bn_choice2d);
		imgBtn_chooseAddr = (ImageButton) findViewById(R.id.bn_choiceAdd);
		imgBtn_scan2d = (ImageButton) findViewById(R.id.bn_scan2d);
		imgBtn_editAddr = (ImageButton) findViewById(R.id.bn_editAdd);

		imgBtn_editAddr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到编辑目的地动作
				Intent toeditAdd = new Intent(FourChoiceActivity.this, EditDesActivity.class);
				startActivity(toeditAdd);
			}
		});

		imgBtn_scan2d.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到扫描二维码动作
				Intent openCameraIntent = new Intent(FourChoiceActivity.this, MipcaActivityCapture.class);
				startActivityForResult(openCameraIntent, SCAN_REQUEST_CODE);
			}
		});

		imgBtn_choice2d.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//跳转到选择二维码图片动作
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, CHOOSEIMG_REQUEST_CODE);
			}

		});
		
		imgBtn_chooseAddr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent toChooseAddr = new Intent(FourChoiceActivity.this,
						ChooseAddrActivity.class);
				startActivityForResult(toChooseAddr, CHOOSE_ADDRESS_CODE);
			}
		});
	}

	/**
	 * 处理返回结果
	 */
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
        	switch (requestCode) {
    		case SCAN_REQUEST_CODE:	//返回的是扫描二维码的结果
    			if(resultCode == RESULT_OK){
    				Bundle bundle = data.getExtras();
    				//显示扫描到的内容
    				String result = bundle.getString("result");
    				//解析扫描结果,提取出经纬度
    				try {
						endLoc = Untilly.parseToLocation(result);
						//获取起点位置信息
						startLoc = (Location) ((PPSApplication)getApplicationContext()).get("startLoc");
						Intent intent = new Intent(FourChoiceActivity.this, RoutePlanDemo.class);
						//将起终点信息传给导航activity
						intent.putExtra("startLocation", startLoc);
						intent.putExtra("endLocation", endLoc);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(FourChoiceActivity.this, "您扫描的二维码不符合要求!", Toast.LENGTH_SHORT).show();
					}
    			}
    			break;
    		case CHOOSEIMG_REQUEST_CODE:	//返回的是选取的二维码图片
    			// 二维码解码配置
				Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
				Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
				decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
				hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
				hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
				// 图片解码
				try {
					Uri uri = data.getData();// 取得图片地址
					//得到解码结果
					Result lResult = new MultiFormatReader().decode(loadImage(uri), hints);
					String result = lResult.getText().toString();
    				//解析扫描结果,提取出经纬度
    				try {
						endLoc = Untilly.parseToLocation(result);
						//获取起点位置信息
						startLoc = (Location) ((PPSApplication)getApplicationContext()).get("startLoc");
						Intent intent = new Intent(FourChoiceActivity.this, RoutePlanDemo.class);
						//将起终点信息传给导航activity
						intent.putExtra("startLocation", startLoc);
						intent.putExtra("endLocation", endLoc);
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(FourChoiceActivity.this, "您扫描的二维码不符合要求!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// 当所选图片不是二维码图片时抛出异常
					e.printStackTrace();
					Toast.makeText(FourChoiceActivity.this, "您选择的不是二维码图片或不是jpg文件", Toast.LENGTH_SHORT).show();
				}
    			break;
    		case CHOOSE_ADDRESS_CODE:	//返回的是选取的address信息
    			Bundle bundle = data.getExtras();
				Address address = (Address) bundle.getSerializable("addressObj");

				//获取起点位置信息
				startLoc = (Location) ((PPSApplication)getApplicationContext()).get("startLoc");
				//获取终点位置信息
				endLoc = new Location();
				endLoc.setAddress(address.getAddress());
				endLoc.setLatitude(address.getLatitude());
				endLoc.setLongitude(address.getLongitude());
				
				Intent intent = new Intent(FourChoiceActivity.this, RoutePlanDemo.class);
				//将起终点信息传给导航activity
				intent.putExtra("startLocation", startLoc);
				intent.putExtra("endLocation", endLoc);
				startActivity(intent);
    			break;
    		default:
    			Toast.makeText(FourChoiceActivity.this, "请重新选取有效的地址信息!", Toast.LENGTH_LONG).show();
    		}
        }
    }
	

	/**
	 * 获取图片
	 * 
	 * @param uri
	 *            图片地址
	 * @param context
	 *            当前上下文
	 * @return
	 * @throws IOException
	 */
	private BinaryBitmap loadImage(Uri uri) throws Exception {
		ContentResolver cr = this.getContentResolver();
		Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
		int lWidth = bitmap.getWidth();
		int lHeight = bitmap.getHeight();
		int[] lPixels = null;
		if (1024 > lHeight && 1024 > lWidth) {

			lPixels = new int[lWidth * lHeight];

		} else {
			lWidth = 200;
			lHeight = 200;
			lPixels = new int[lWidth * lHeight];
			;
		}
		bitmap.getPixels(lPixels, 0, lWidth, 0, 0, lWidth, lHeight);
		return new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(
				lWidth, lHeight, lPixels)));

	}

	// ///////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////框架
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
