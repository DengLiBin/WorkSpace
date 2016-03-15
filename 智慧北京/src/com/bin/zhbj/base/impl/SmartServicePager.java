package com.bin.zhbj.base.impl;

import com.bin.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	
	}
//ContentFragment中调用
	@Override
	public void initData() {
		super.setSlidingMenuEnable(true);
		tvTitle.setText("智慧服务");
		TextView text=new TextView(mActivity);
		text.setText("生活");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);//将textView添加到frameLayout中
	}

}
