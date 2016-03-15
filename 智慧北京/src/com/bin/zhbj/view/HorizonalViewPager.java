package com.bin.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 11新闻页子页签的viewPager 
 * @author Administrator
 *
 */
public class HorizonalViewPager extends ViewPager {

	public HorizonalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizonalViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(this.getCurrentItem()!=0){//若果当前页不是第0页，就向父控件请求被拦截的事件（滑动），让该控件自己处理；若果当前页是第0页，滑动事件就让父控件拦截处理
			getParent().requestDisallowInterceptTouchEvent(true);//用getParent取请求
		}
		
		return super.dispatchTouchEvent(ev);
	}
}
