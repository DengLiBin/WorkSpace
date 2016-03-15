package com.itheima.pushdemo;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义application
 * 
 * @author Kevin
 * 
 */
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("应用创建啦....");
		
		JPushInterface.setDebugMode(true);
	    JPushInterface.init(this);
	}
	
	public void doSomething() {
		System.out.println("do something...");
	}

}
