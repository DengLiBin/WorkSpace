package com.bin.baidumap;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.RoutePlanSearch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class HelloWord extends Activity {

	protected MyBaiduSdkReceiver mBaiduSdkReceiver;
	protected BaiduMap mBaiduMap;//管理具体的某一个MapView，旋转，移动，缩放
	protected MapView mMapView;
	protected double latitude=40.050966;//纬度
	protected double longitude=116.303128;//经度
	protected LatLng hmPos = new LatLng(latitude, longitude);//坐标对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initManager();
		
		setContentView(R.layout.activity_helloword);
		
		init();
	
		PoiSearch poiSearch=PoiSearch.newInstance();
		RoutePlanSearch routePlanSearch= RoutePlanSearch.newInstance();
		//拿到启动该Activity的Activity传过来的数据
		Intent intent=getIntent();
		String value=intent.getStringExtra("whatMap");
		
		// 设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图
		if(value.equals("BaseMap")){
			//底图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			mBaiduMap.setTrafficEnabled(false);
		}else if(value.equals("satelliteMap")){
			// 卫星图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			mBaiduMap.setTrafficEnabled(false);
		}else if(value.equals("trafficMap")){
			// 交通图
			// 是否打开交通图层
			mBaiduMap.setTrafficEnabled(true);
			
		}else if(value.equals("optionMap")){//画圆形覆盖物
			 OptionDemo option=new OptionDemo();
			 option.drawCircle(mBaiduMap, hmPos);
			 
		}else if(value.equals("textOptionMap")){//文字覆盖物
			 OptionDemo option=new OptionDemo();
			 option.drawText(mBaiduMap, hmPos);
			 
		}else if(value.equals("markOptionMap")){//mark覆盖物
			 OptionDemo option=new OptionDemo(mMapView,getApplicationContext(), hmPos);
			 option.drawMark(mBaiduMap, hmPos);
		}else if(value.equals("searchResult")){//poi搜索
			
			PoiSearchDemo poiSearchDemo=new PoiSearchDemo(poiSearch,getApplicationContext(),mBaiduMap);
			//poiSearcherDemo.search();//范围内搜索
			//poiSearchDemo.searchNear(hmPos);//周边搜索
			poiSearchDemo.searchInCity("成都","医院");//城市搜索
		}else if(value.equals("route")){
			//驾车路线
			DrivingRouteOverlayDemo route=new DrivingRouteOverlayDemo(getApplicationContext(), mBaiduMap, routePlanSearch);
			route.search(hmPos);
		}else if(value.equals("walkRoute")){
			//步行路线
			DrivingRouteOverlayDemo drivingRouteOverlayDemo=new DrivingRouteOverlayDemo(getApplicationContext(), mBaiduMap, routePlanSearch);
			drivingRouteOverlayDemo.search(hmPos);
			
		}else if(value.equals("transitRoute")){//公交路线
			TransitRouteOverlayDemo transitRouteOverlayDemo=new TransitRouteOverlayDemo(routePlanSearch,getApplicationContext(), mBaiduMap);
			transitRouteOverlayDemo.search(hmPos);
		}else if(value.equals("location")){
			LocationDemo locationDemo=new LocationDemo(getApplicationContext(), mBaiduMap, routePlanSearch);
			locationDemo.locate();
		}
		
	}
	
	
	protected void init(){
		//设置地图级别(V2.X 3-19; V1.X 3-18)
		
		mMapView = (MapView) findViewById(R.id.mv_map);
		mBaiduMap=mMapView.getMap();//拿到BaiduMap对象
		
		//描述地图将要发生的变化，使用工厂类MapStatusUpdateFactory创建
		MapStatusUpdate mapStatusUpdateLevel = MapStatusUpdateFactory.zoomTo(19);
		//设置缩放级别
		mBaiduMap.setMapStatus(mapStatusUpdateLevel);
		
		//设置中心点
		LatLng latng=new LatLng(latitude,longitude);//坐标对象
		MapStatusUpdate mapStatusUpdatePoint=MapStatusUpdateFactory.newLatLng(latng);
		mBaiduMap.setMapStatus(mapStatusUpdatePoint);
		
		mMapView.showZoomControls(false);//默认是true,显示缩放按钮
		mMapView.showScaleControl(false);//默认是true,显示标尺
		
	}
	/**
	 * 初始化地图
	 */
	protected void initManager() {
		SDKInitializer.initialize(getApplicationContext());//不能用this,必须是全局的Context
		mBaiduSdkReceiver = new MyBaiduSdkReceiver();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);//网络错误发送广播
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);//KEY校验失败，发送广播
		
		registerReceiver(mBaiduSdkReceiver, filter);//代码注册广播接受者
	}
	/**
	 * 广播接收者
	 *接收BaiDuSDK发的广播（网络错误和key校验失败）
	 */
	class MyBaiduSdkReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String result=intent.getAction();
			if(result==SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR){
				//网络错误
				Toast.makeText(getApplicationContext(), "无网络", 0).show();
			}else if(result==SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR){
				//key校验失败
				Toast.makeText(getApplicationContext(), "校验失败", 0).show();
			}
		}
		
	}
	
	/**
	 * 键盘按键监听（模拟器）
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_1:
			//放大
			// 放大地图缩放级别 每次放大一个级别
			MapStatusUpdate zoomInStatus=MapStatusUpdateFactory.zoomIn();
			mBaiduMap.setMapStatus(zoomInStatus);
			break;
		case KeyEvent.KEYCODE_2:
			// 缩小
			// 缩小地图缩放级别 每次缩小一个级别
			MapStatusUpdate zooOutStatus = MapStatusUpdateFactory.zoomOut();
			mBaiduMap.setMapStatus(zooOutStatus);
			break;
		case KeyEvent.KEYCODE_3:
			// 以一个点为中心 旋转
			MapStatus mapStatus = mBaiduMap.getMapStatus();// 获取地图的当前状态
			float rotate = mapStatus.rotate;
			// 旋转 范围0~360
			MapStatus rotateStatus = new  MapStatus.Builder().rotate(rotate+30).build();
			// 创建新的MapStatus
			MapStatusUpdate rotateStatusUpdate = MapStatusUpdateFactory.newMapStatus(rotateStatus);
			mBaiduMap.setMapStatus(rotateStatusUpdate);
			break;
		case KeyEvent.KEYCODE_4:
			// 以一条直线 为轴 旋转 Overlooking 俯角
			MapStatus mapStatusOver = mBaiduMap.getMapStatus();// 获取地图的当前状态
			float overlook = mapStatusOver.overlook;
			// 0~45
			MapStatus overStatus = new MapStatus.Builder().overlook(overlook-5).build();
			MapStatusUpdate overStatusUpdate = MapStatusUpdateFactory.newMapStatus(overStatus);
			mBaiduMap.setMapStatus(overStatusUpdate);		
			break;
		case KeyEvent.KEYCODE_5:
			// 移动
			MapStatusUpdate moveStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(40.065796,116.349868));
			// 带动画的更新地图状态 耗时300毫秒
			mBaiduMap.animateMapStatus(moveStatusUpdate);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		mMapView.onResume();//生命周期方法
		super.onResume();
	}
	@Override
	protected void onPause() {
		mMapView.onPause();//生命周期方法
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(mBaiduSdkReceiver);
		mMapView.onDestroy();//生命周期方法
		super.onDestroy();
	}
	
	
}
