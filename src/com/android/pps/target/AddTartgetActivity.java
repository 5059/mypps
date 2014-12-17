package com.android.pps.target;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.pps.LocationService;
import com.android.pps.R;

public class AddTartgetActivity extends ActionBarActivity {

	private ImageButton imgBtn_ok, imgBtn_chooseAddr;
	private EditText editT_address;
	private String addrStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_getadd);

		editT_address = (EditText) findViewById(R.id.et_add);
		imgBtn_ok = (ImageButton) findViewById(R.id.bn_ok);
		imgBtn_chooseAddr = (ImageButton) findViewById(R.id.choice_add);
		
		imgBtn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toAddInfo = new Intent(AddTartgetActivity.this,
						EditTargetActivity.class);
				addrStr = editT_address.getText().toString();
				if("".equals(addrStr) || null == addrStr){
					Toast.makeText(AddTartgetActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bundle = getIntent().getExtras();
				bundle.putString("address", addrStr);
				toAddInfo.putExtras(bundle);
				startActivity(toAddInfo);
				finish();
			}
		});
		
		imgBtn_chooseAddr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent toChooseAddr = new Intent(AddTartgetActivity.this,
						ChooseAddrActivity.class);
				startActivity(toChooseAddr);
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
