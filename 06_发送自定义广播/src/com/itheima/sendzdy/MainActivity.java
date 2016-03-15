package com.itheima.sendzdy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View v){
    	//发送自定义广播
    	Intent intent = new Intent();
    	//广播中的action也是自定义的
    	intent.setAction("com.itheima.zdy");
    	sendBroadcast(intent);
    }
    
}
