package com.bin.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

public class MainActivity extends Activity {
	private Intent intent,intent2;
	private MyServiceConn conn;
	PMin pm;//接口的子类对象。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent=new Intent(this,MyService.class);
        intent2=new Intent(this,LeadService.class);
        
        conn = new MyServiceConn();
      //绑定领导服务（LeadService）
        this.bindService(intent2, conn, this.BIND_AUTO_CREATE);
    }
    public void banZheng(View v){
    	
        pm.QianXian();
    }
    public void clickStart(View v){
    	//显式（指定了服务的类名）启动服务,调用服务的onCreate和onStartCommand方法；
    	this.startService(intent);
    }
    public void clickStop(View v){
    	//调用服务的onDestroy方法
    	this.stopService(intent);
    }
    
    public void bind(View v){
    	//绑定服务，调用服务的onCreate和onBind方法
    	this.bindService(intent, conn, BIND_AUTO_CREATE);
    }
    public void unbind(View v){
    	//解绑，调用服务的onUnBind和onDestroy方法
    	this.unbindService(conn);
    }
    
    class MyServiceConn implements ServiceConnection{
    	
    	//服务连接成功时，此方法调用
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			pm=(PMin) service;
		}
		
		//服务失去连接时，此方法调用
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
