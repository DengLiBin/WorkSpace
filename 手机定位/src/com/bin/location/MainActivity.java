package com.bin.location;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//获取系统的定位服务
		LocationManager lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> allProvider=lm.getAllProviders();//获取所有的位置提供者
		System.out.println(allProvider);
		MyLocationListener listener=new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
	}
	
	class MyLocationListener implements LocationListener{
		//位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("onLocatonChanged");
			String j="经度："+location.getLongitude();
			String w="纬度："+location.getLatitude();
			
			String accuracy="精确度："+location.getAccuracy();
			String altitude="海拔："+location.getAltitude();
			System.out.println("经度："+j+"纬度："+w+"精确度："+accuracy+"海拔："+altitude);
		}
		// 用户关闭gps
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		// 用户打开gps
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		//位置提供者状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}
		
	}
}
