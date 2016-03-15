package com.bin.itheima;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View v){
    	Intent intent=new Intent();
    	//发送有序广播
    	//resultReceiver:不需要在清单文件中配置，这个广播接收者只接受该条有序广播，并且是最后一个收到该广播，并且一定可以收到该广播
    	sendOrderedBroadcast(intent, null,new MyReceiver(),null,0,"没人发一百斤大米",null);
    }
    class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text=getResultData();
			System.out.println("反贪局收到文件："+text);
		}
    	
    }
}
