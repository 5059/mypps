package com.android.pps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.pps.db.DBOpenHelper;
import com.android.pps.navi.FourChoiceActivity;
import com.android.pps.target.AddTartgetActivity;
import com.android.pps.util.Location;
import com.android.pps.util.StateCheck;

public class MainActivity extends ActionBarActivity {

	public static String PATH_IMG = "/pps/image/";
	public static String PATH_CACHE = "/pps/cache/";
	
	private ImageButton imgBtn_nav, imgBtn_target;
	
	private Location startLoc = null, endLoc = null;
	private DBOpenHelper dbHelper;
	private SQLiteDatabase db = null; 
	private ProgressDialog proDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		openDB();
		
		imgBtn_nav = (ImageButton) findViewById(R.id.bn_nav);
		imgBtn_target = (ImageButton) findViewById(R.id.bn_target);
		
		//检测是否联网
        if(!StateCheck.isNetworkConnected(this)){
        	Toast.makeText(this, "网络未连接，请检查网络设置后重试！", Toast.LENGTH_SHORT).show();
        }
		
		LocationService locationService = new LocationService(this);
		
		imgBtn_nav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainTonav = new Intent(MainActivity.this, FourChoiceActivity.class);
				startActivity(mainTonav);
			}
		});

		imgBtn_target.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainToTag = new Intent(MainActivity.this, AddTartgetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("startLocation", startLoc);
				mainToTag.putExtras(bundle);
				startActivity(mainToTag);
			}
		});
		
		proDialog = android.app.ProgressDialog.show(MainActivity.this, "Loading", "正在初始化当前位置!");
	}
	
	public void dismissDialog(){
		proDialog.dismiss();
	}
	
    private void openDB(){
    	dbHelper = new DBOpenHelper(this);
    	db = dbHelper.getWritableDatabase();
    }

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
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
