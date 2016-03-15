package com.bin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//服务对象时系统创建的，不能自己new一个服务对象，在Activity中是拿不到服务对象的。
public class MyService extends Service {
	//绑定时调用,返回的是一个中间对象，在服务对象和访问该服务的Activity之间的对象（不需要改对象就直接返回null）
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("onBind方法");
		return null;
	}
	//解绑时调用
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("unbind方法");
		return super.onUnbind(intent);
	}
	//创建服务,在绑定（onBind）服务和开始服务(onStartCommand)之前创建
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("create方法");
	}

	//开启服务
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("startCommand方法");
		return super.onStartCommand(intent, flags, startId);
	}
	
	//停止服务调用此方法
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("destroy方法");
	}


}
/*#两种启动方法混合使用
* 用服务实现音乐播放时，因为音乐播放必须运行在服务进程中（与Activity无关），可是音乐服务中的方法，需要被前台Activity（与Activity有关）所调用，所以需要混合启动音乐服务
* 先start，再bind，销毁时先unbind，在stop
* */
