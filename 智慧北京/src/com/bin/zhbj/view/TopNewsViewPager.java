package com.bin.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;
	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}
	/**
	 * 事件分发，请求父控件及其祖宗控件是否拦截事件
	 * 1、右滑，当滑动到第一个页面时，需要父控件拦截（滑动事件交给父控件处理）
	 * 2、左滑，当滑动到最后一个页面时，也需要父控件处理
	 * 3、上下滑，也需要父控件处理
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		//getParent().requestDisallowInterceptTouchEvent(true);//用getParent取请求
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);//获取事件（父控件不要拦截），不然MotionEvent.ACTION_MOVE不会执行
			startX = (int)ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX=(int) ev.getRawX();
			int endY=(int) ev.getRawY();
			if(Math.abs(endX-startX)>Math.abs(endY-startY)){//水平滑动
				if(endX>startX){//右滑
					if(getCurrentItem()==0){
						getParent().requestDisallowInterceptTouchEvent(false);//需要父控件拦截
					}
				}else{//左滑
					if(getCurrentItem()==getAdapter().getCount()-1){//最后一个页面
						getParent().requestDisallowInterceptTouchEvent(false);//需要父控件拦截
					}
				}
			}else{//垂直滑动
				getParent().requestDisallowInterceptTouchEvent(false);//需要父控件拦截
			}
			break;
		default:
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}

}
