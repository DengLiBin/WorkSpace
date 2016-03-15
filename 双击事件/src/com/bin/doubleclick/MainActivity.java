package com.bin.doubleclick;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	long firstTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	public void click(View v){
		if(firstTime>0){// 发现之前点击过一次
			if(System.currentTimeMillis()-firstTime<500){
				Toast.makeText(this, "双击啦", Toast.LENGTH_SHORT);
				firstTime=0;//重置时间, 重新开始
				return;
			}
		}
		firstTime=System.currentTimeMillis();
	}
}
