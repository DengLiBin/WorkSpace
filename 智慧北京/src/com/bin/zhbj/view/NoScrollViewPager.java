package com.bin.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}
	//返回false,表示不拦截事件（不然它的子控件拿不到事件）
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	};
	@Override
	//拦截滑动监听，不做任何处理
	public boolean onTouchEvent(MotionEvent arg0) {
		
		return false;
	}

	
}
