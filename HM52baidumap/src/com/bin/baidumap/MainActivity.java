package com.bin.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	//进入底图
	public void enterMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "baseMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//进入卫星图
	public void enterSatelliteMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "satelliteMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//进入交通图
	public void enterTrafficMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "trafficMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//有圆形覆盖物的底图
	public void enterOptionsMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "optionMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//有文字覆盖物的底图
	public void enterTextOptionsMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "textOptionMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//mark覆盖物
	public void enterMarkOptionsMap(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "markOptionMap");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//搜索结果
	public void searchResult(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "searchResult");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//驾车路线
	public void route(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "route");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//步行路线
	public void walkRoute(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "walkRoute");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//公交路线
	public void transitRoute(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "transitRoute");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}
	//定位
	public void location(View v){
		Intent intent=new Intent();
		intent.putExtra("whatMap", "location");
		intent.setClass(MainActivity.this, HelloWord.class);
		startActivity(intent);
	}

}
