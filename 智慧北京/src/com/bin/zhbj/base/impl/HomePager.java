package com.bin.zhbj.base.impl;

import java.io.File;
import java.io.FileOutputStream;

import com.bin.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {
	public HomePager(Activity activity) {
		super(activity);
		
	}
	
//ContentFragment中调用	
	@Override
	public void initData() {
		super.setSlidingMenuEnable(false);
		imgBtn.setVisibility(View.INVISIBLE);//隐藏按钮
		tvTitle.setText("智慧北京");
		TextView text=new TextView(mActivity);
		text.setText("首页");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);//将textView添加到frameLayout中
		
		String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache_52";
		//String path="/storage/sdcard1/zhbj_cache_52";
		System.out.println("path:"+path);
		File file=new File(path,"denglibin.txt");
		File parentFile=file.getParentFile();
		if(!parentFile.exists()){//如果文件夹不存在，创建文件夹
			parentFile.mkdir();
		}
		try{
			FileOutputStream out=new FileOutputStream(file);
			out.write("字符串".getBytes());
			out.close();
			System.out.println("路径存在");
		}catch(Exception e){
			System.out.println("路径不存在");
			e.printStackTrace();
		}
		
	}

}
