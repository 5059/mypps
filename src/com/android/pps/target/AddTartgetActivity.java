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

import com.android.pps.R;
import com.android.pps.db.SqliteUnitl;
import com.android.pps.util.Address;

public class AddTartgetActivity extends ActionBarActivity {

	private ImageButton imgBtn_ok, imgBtn_chooseAddr;
	private EditText editT_address;
	private String addrStr;
	private final static int CHOOSE_ADDRESS_CODE = 2;
	
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
				addrStr = editT_address.getText().toString();
				if("".equals(addrStr) || null == addrStr){
					Toast.makeText(AddTartgetActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
					return;
				}else if(SqliteUnitl.isExistedAddress(AddTartgetActivity.this, addrStr)){	//不存在同名地址才能继续
					Toast.makeText(AddTartgetActivity.this, "地址名已存在", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent toAddInfo = new Intent(AddTartgetActivity.this,
						EditTargetActivity.class);
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
				startActivityForResult(toChooseAddr, CHOOSE_ADDRESS_CODE);
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
	
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	 if(resultCode == RESULT_OK){
         	if(requestCode == CHOOSE_ADDRESS_CODE) {
         		Bundle dataBundle = data.getExtras();
				Address address = (Address) dataBundle.getSerializable("addressObj");
         		
				Intent toAddInfo = new Intent(AddTartgetActivity.this,
						EditTargetActivity.class);
				Bundle bundle = getIntent().getExtras();
				bundle.putSerializable("addressObj", address);
				toAddInfo.putExtras(bundle);
				startActivity(toAddInfo);
				finish();
         	}
         }
    }
}
