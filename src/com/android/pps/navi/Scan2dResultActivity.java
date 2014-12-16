package com.android.pps.navi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pps.R;
import com.android.pps.util.QRCodeService;

public class Scan2dResultActivity extends ActionBarActivity {
	
	private TextView scanReslt;
	private Bitmap bitmap;
	private ImageView iv_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_reslt_temp);
		scanReslt = (TextView) findViewById (R.id.scan_reslt);
		iv_result = (ImageView) findViewById(R.id.iv_result);
		
        
	    Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String scanResult = bundle.getString("result");
		bitmap = QRCodeService.CreateQRCode(scanResult, null);
		iv_result.setImageBitmap(bitmap);
		scanReslt.setText(scanResult);
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
