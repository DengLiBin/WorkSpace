package com.bin.servicebroadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		intent=new Intent(this.getApplicationContext(),RegisterService.class);
		
	}

	public void start(View v){
		this.startService(intent);
	}
	public void stop(View v){
		this.stopService(intent);
	}
	
}
