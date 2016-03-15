package com.bin.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LeadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		// 返回一个Binder对象，这个对象就是中间人对象
		return new ZhouMi();
	}
	
	public static void banzheng(){
		System.out.println("领导帮你来办证");
	}
	class ZhouMi extends Binder implements PMin{
		@Override
		public void QianXian(){
			LeadService.banzheng(); 
		}
		public  void daMaJiang(){
			System.out.println("陪领导打麻将");
		}
	}

}
