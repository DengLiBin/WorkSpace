package com.itheima.applistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class APPStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Uri uri = intent.getData();
		if("android.intent.action.PACKAGE_ADDED".equals(action)){
			Toast.makeText(context, uri.toString() + "被安装了", 0).show();
		}
		if("android.intent.action.PACKAGE_REPLACED".equals(action)){
			Toast.makeText(context, uri.toString() + "被升级了", 0).show();
		}
		if("android.intent.action.PACKAGE_REMOVED".equals(action)){
			Toast.makeText(context, uri.toString() + "被卸载了", 0).show();
		}
	}

}
