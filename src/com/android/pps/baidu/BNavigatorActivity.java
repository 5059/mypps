package com.android.pps.baidu;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.setting.BNSettingManager;
import com.baidu.navisdk.comapi.setting.SettingParams;
import com.baidu.navisdk.comapi.tts.BNTTSPlayer;
import com.baidu.navisdk.comapi.tts.BNavigatorTTSPlayer;
import com.baidu.navisdk.comapi.tts.IBNTTSPlayerListener;
import com.baidu.navisdk.model.datastruct.LocData;
import com.baidu.navisdk.model.datastruct.SensorData;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.routeguide.IBNavigatorListener;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.ui.widget.RoutePlanObserver.IJumpToDownloadListener;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;

public class BNavigatorActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.i("BNavigator", "a onCreate");
		//创建NmapView
		if (Build.VERSION.SDK_INT < 14) {
            BaiduNaviManager.getInstance().destroyNMapView();
        }
		MapGLSurfaceView nMapView = BaiduNaviManager.getInstance().createNMapView(this); 
		BNSettingManager.getInstance(this).setNaviDayAndNightMode(SettingParams.Action.DAY_NIGHT_MODE_DAY);
		
		//创建导航视图
		View navigatorView = BNavigator.getInstance().init(BNavigatorActivity.this, getIntent().getExtras(), nMapView);
		
		//填充视图
		setContentView(navigatorView);
		BNavigator.getInstance().setListener(mBNavigatorListener);
		BNavigator.getInstance().startNav();
		
		// 初始化TTS. 开发者也可以使用独立TTS模块，不用使用导航SDK提供的TTS
		BNTTSPlayer.initPlayer();
		//设置TTS播放回调
		BNavigatorTTSPlayer.setTTSPlayerListener(new IBNTTSPlayerListener() {
            
            @Override
            public int playTTSText(String arg0, int arg1) {
            	//开发者可以使用其他TTS的API
            	Log.i("BNavigator", "a playTTSText");
                return BNTTSPlayer.playTTSText(arg0, arg1);
                
            }
            
            @Override
            public void phoneHangUp() {
                //手机挂断
            	Log.i("BNavigator", "a phoneHangUp");
            }
            
            @Override
            public void phoneCalling() {
                //通话中
            	Log.i("BNavigator", "a phoneCalling");
            }
            
            @Override
            public int getTTSState() {
            	//开发者可以使用其他TTS的API,
            	Log.i("BNavigator", "a getTTSState");
                return BNTTSPlayer.getTTSState();
            }
        });
		
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, new IJumpToDownloadListener() {
			
			@Override
			public void onJumpToDownloadOfflineData() {
				// TODO Auto-generated method stub
				Log.i("BNavigator", "a onJumpToDownloadOfflineData");
			}
		}));
		
	}
	
	private IBNavigatorListener mBNavigatorListener = new IBNavigatorListener() {
        
        @Override
        public void onYawingRequestSuccess() {
            // TODO 偏航请求成功
        	Log.i("BNavigator", "a onYawingRequestSuccess");
        }
        
        @Override
        public void onYawingRequestStart() {
            // TODO 开始偏航请求
        	Log.i("BNavigator", "a onYawingRequestStart");
        }
        
        @Override
        public void onPageJump(int jumpTiming, Object arg) {
            // TODO 页面跳转回调
        	Log.i("BNavigator", "a onPageJump");
        	if(IBNavigatorListener.PAGE_JUMP_WHEN_GUIDE_END == jumpTiming){
        	    finish();
        	}else if(IBNavigatorListener.PAGE_JUMP_WHEN_ROUTE_PLAN_FAIL == jumpTiming){
        		finish();
        	}
        }

		@Override
		public void notifyGPSStatusData(int arg0) {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifyGPSStatusData");
		}

		@Override
		public void notifyLoacteData(LocData arg0) {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifyLoacteData");
		}

		@Override
		public void notifyNmeaData(String arg0) {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifyNmeaData");
		}

		@Override
		public void notifySensorData(SensorData arg0) {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifySensorData");
		}

		@Override
		public void notifyStartNav() {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifyStartNav");
			BaiduNaviManager.getInstance().dismissWaitProgressDialog();
		}

		@Override
		public void notifyViewModeChanged(int arg0) {
			// TODO Auto-generated method stub
			Log.i("BNavigator", "a notifyViewModeChanged");
		}
        
    };
	    
	@Override
    public void onResume() {
		Log.i("BNavigator", "a onResume");
        BNavigator.getInstance().resume();
        super.onResume();
        BNMapController.getInstance().onResume();
    };

    @Override
    public void onPause() {
    	Log.i("BNavigator", "a onPause");
        BNavigator.getInstance().pause();
        super.onPause();
        BNMapController.getInstance().onPause();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	Log.i("BNavigator", "a onConfigurationChanged");
    	BNavigator.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
    
    public void onBackPressed(){
    	Log.i("BNavigator", "a onBackPressed");
        BNavigator.getInstance().onBackPressed();
    }
    
    @Override
    public void onDestroy(){
    	Log.i("BNavigator", "a onDestroy");
    	BNavigator.destory();
		BNRoutePlaner.getInstance().setObserver(null);
    	super.onDestroy();
    }
}
