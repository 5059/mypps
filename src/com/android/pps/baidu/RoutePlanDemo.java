package com.android.pps.baidu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.pps.R;
import com.android.pps.util.Location;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.CommonParams.NL_Net_Mode;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams.Const.LayerMode;
import com.baidu.navisdk.comapi.routeguide.RouteGuideParams.RGLocationMode;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.comapi.setting.BNSettingManager;
import com.baidu.navisdk.comapi.setting.SettingParams;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.routeguide.BNavConfig;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.util.common.CoordinateTransformUtil;
import com.baidu.navisdk.util.common.PreferenceHelper;
import com.baidu.navisdk.util.common.ScreenUtil;
import com.baidu.nplatform.comapi.basestruct.GeoPoint;
import com.baidu.nplatform.comapi.map.ItemizedOverlay;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;
import com.baidu.nplatform.comapi.map.Overlay;
import com.baidu.nplatform.comapi.map.OverlayItem;

/**
 * 通过定位结果显示地图,并规划到目标点的线路,最后进行导航
 * @author Administrator
 *
 */
public class RoutePlanDemo extends Activity {
	private RoutePlanModel mRoutePlanModel = null;
	private MapGLSurfaceView mMapView = null;
	private Location startLocation, endLocation;
	
	/**
	 * 起终点坐标地址,x表示纬度(latitude),y表示经度(longitude)
	 */
	private int startX, startY, endX, endY;
	private Spinner routeMode;
	private int routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND;
	
	public void onCreate(Bundle savedInstance) {
		Log.i("RoutePlan", "a onCreate");
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_routeplan);
		//填充Spinner的路径模式数据
		routeMode = (Spinner)findViewById(R.id.routeMode);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
				,new String[]{"推荐路径", "最短距离", "最短时间", "最少收费 ", "避免堵塞"});
		routeMode.setAdapter(adapter);
		
		//获取起终点坐标
		startLocation = (Location)getIntent().getSerializableExtra("startLocation");
		endLocation = (Location)getIntent().getSerializableExtra("endLocation");
		startX = (int) (startLocation.getLatitude() * 1e5);
		startY = (int) (startLocation.getLongitude() * 1e5);
		endX = (int) (endLocation.getLatitude() * 1e5);
		endY = (int) (endLocation.getLongitude() * 1e5);
		
		routeMode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				switch (position) {
					case 0:
						routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND;
						break;
					case 1:
						routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_DIST;
						break;
					case 2:
						routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME;
						break;
					case 3:
						routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TOLL;
						break;
					case 4:
						routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_AVOID_TAFFICJAM;
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				routePlanMode = NE_RoutePlan_Mode.ROUTE_PLAN_MOD_RECOMMEND;
			}
		});

		findViewById(R.id.online_calc_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startCalcRoute(NL_Net_Mode.NL_Net_Mode_OnLine);
			}
		});
		
		findViewById(R.id.simulate_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startNavi(false);
					}
				});

		findViewById(R.id.real_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PreferenceHelper.getInstance(getApplicationContext())
						.putBoolean(SettingParams.Key.SP_TRACK_LOCATE_GUIDE,
								false);
				startNavi(true);
			}
		});
	}
    
	@Override
	public void onDestroy() {
		Log.i("RoutePlan", "a onDestroy");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		Log.i("RoutePlan", "a onPause");
		super.onPause();
		BNRoutePlaner.getInstance().setRouteResultObserver(null);
		((ViewGroup) (findViewById(R.id.mapview_layout))).removeAllViews();
		BNMapController.getInstance().onPause();
	}

	@Override
	public void onResume() {
		Log.i("RoutePlan", "a onResume");
		super.onResume();
		initMapView();
		((ViewGroup) (findViewById(R.id.mapview_layout))).addView(mMapView);
		BNMapController.getInstance().onResume();
	}

    private void initMapView() {
    	Log.i("RoutePlan", "a initMapView");
        if (Build.VERSION.SDK_INT < 14) {
            BaiduNaviManager.getInstance().destroyNMapView();
        }
        
        mMapView = BaiduNaviManager.getInstance().createNMapView(this);
        BNMapController.getInstance().setLevel(14);	//BNMapController地图管理控制类，控制地图的缩放、移动、旋转等操作 
        BNMapController.getInstance().setLayerMode(
                LayerMode.MAP_LAYER_MODE_BROWSE_MAP);
        updateCompassPosition();
        BNSettingManager.getInstance(this).setNaviDayAndNightMode(SettingParams.Action.DAY_NIGHT_MODE_DAY);

        BNMapController.getInstance().locateWithAnimation(startY, startX);
    }
	
	/**
	 * 更新指南针位置
	 */
	private void updateCompassPosition(){
		Log.i("RoutePlan", "a updateCompassPosition");
		int screenW = this.getResources().getDisplayMetrics().widthPixels;
		BNMapController.getInstance().resetCompassPosition(
				screenW - ScreenUtil.dip2px(this, 30),
					ScreenUtil.dip2px(this, 126), -1);
	}

	private void startCalcRoute(int netmode) {
		Log.i("RoutePlan", "a startCalcRoute");

		//起点
		RoutePlanNode startNode = new RoutePlanNode(startX, startY,
				RoutePlanNode.FROM_MY_POSITION, "华侨城", "华侨城");
		//终点
		RoutePlanNode endNode = new RoutePlanNode(endX, endY,
				RoutePlanNode.FROM_MAP_POINT, "厦门大学", "厦门大学");
		//将起终点添加到nodeList
		ArrayList<RoutePlanNode> nodeList = new ArrayList<RoutePlanNode>(2);
		nodeList.add(startNode);
		nodeList.add(endNode);
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, null));
		//设置算路方式
		BNRoutePlaner.getInstance().setCalcMode(this.routePlanMode);
		// 设置算路结果回调
		BNRoutePlaner.getInstance().setRouteResultObserver(mRouteResultObserver);
		// 设置起终点并算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
				nodeList, netmode);
		if(!ret){
			Toast.makeText(this, "规划失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void startNavi(boolean isReal) {
		Log.i("RoutePlan", "a startNavi");
		if (mRoutePlanModel == null) {
			Toast.makeText(this, "请先算路！", Toast.LENGTH_LONG).show();
			return;
		}
		// 获取路线规划结果起点
		RoutePlanNode startNode = mRoutePlanModel.getStartNode();
		// 获取路线规划结果终点
		RoutePlanNode endNode = mRoutePlanModel.getEndNode();
		if (null == startNode || null == endNode) {
			return;
		}
		// 获取路线规划算路模式
		int calcMode = BNRoutePlaner.getInstance().getCalcMode();
		Bundle bundle = new Bundle();
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_VIEW_MODE,
				BNavigator.CONFIG_VIEW_MODE_INFLATE_MAP);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_DONE,
				BNavigator.CONFIG_CLACROUTE_DONE);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_X,
				startNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_Y,
				startNode.getLatitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_X, endNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_Y, endNode.getLatitudeE6());
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_START_NAME,
				mRoutePlanModel.getStartName(this, false));
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_END_NAME,
				mRoutePlanModel.getEndName(this, false));
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_MODE, calcMode);
		if (!isReal) {
			// 模拟导航
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE,
					RGLocationMode.NE_Locate_Mode_RouteDemoGPS);
		} else {
			// GPS 导航
			bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE,
					RGLocationMode.NE_Locate_Mode_GPS);
		}
		
		Intent intent = new Intent(RoutePlanDemo.this, BNavigatorActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private IRouteResultObserver mRouteResultObserver = new IRouteResultObserver() {

		@Override
		public void onRoutePlanYawingSuccess() {
			// TODO Auto-generated method stub
			Log.i("RoutePlan", "a onRoutePlanYawingSuccess");
		}

		@Override
		public void onRoutePlanYawingFail() {
			// TODO Auto-generated method stub
			Log.i("RoutePlan", "a onRoutePlanYawingFail");
		}

		@Override
		public void onRoutePlanSuccess() {
			Log.i("RoutePlan", "a onRoutePlanSuccess");
			BNMapController.getInstance().setLayerMode(
					LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
			mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
					.getModel(ModelName.ROUTE_PLAN);
			Toast.makeText(RoutePlanDemo.this, "路径全程" + mRoutePlanModel.getDistance() + ",大约" + mRoutePlanModel.getTotalTime(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRoutePlanFail() {
			// TODO Auto-generated method stub
			Log.i("RoutePlan", "a onRoutePlanFail");
		}

		@Override
		public void onRoutePlanCanceled() {
			// TODO Auto-generated method stub
			Log.i("RoutePlan", "a onRoutePlanCanceled");
		}

		@Override
		public void onRoutePlanStart() {
			// TODO Auto-generated method stub
			Log.i("RoutePlan", "a onRoutePlanStart");

		}

	};
}
