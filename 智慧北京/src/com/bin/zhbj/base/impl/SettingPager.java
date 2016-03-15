package com.bin.zhbj.base.impl;

import com.bin.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
		
	}
//ContentFragment中调用
	@Override
	public void initData() {
		super.setSlidingMenuEnable(false);
		imgBtn.setVisibility(View.INVISIBLE);//隐藏按钮
		tvTitle.setText("设置中心");
		TextView text=new TextView(mActivity);
		text.setText("设置");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);//将textView添加到frameLayout中
	}
	
}
