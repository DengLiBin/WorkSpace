package com.bin.servicebroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver  {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action=intent.getAction();
		if(Intent.ACTION_SCREEN_OFF.equals(action)){
			System.out.println("屏幕打开");
			Log.i("tag", "屏幕打开");
		}
		else if(Intent.ACTION_SCREEN_ON.equals(action)){
			System.out.println("屏幕关闭");
			Log.i("tag", "屏幕关闭");
		}
		
	}

}
