package com.bin.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * ip拨号
 * @author Administrator
 *
 */
public class CallReceiver extends BroadcastReceiver {
	//接收到广播时会调用
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("adv","abc打电话广播开启");
		//添加IP线路
		//在打电话广播中，会携带拨打的电话的号码，通过以下代码获取到
		String number=this.getResultData();
		SharedPreferences sp=context.getSharedPreferences("ip",Context.MODE_PRIVATE);
		String ipNumber=sp.getString("ipNumber","");
		//把IP线路号码添加至用户拨打号码的前面
		number=ipNumber+number;
		//把新的号码重新放入广播中
		setResultData(number);
	}

}
