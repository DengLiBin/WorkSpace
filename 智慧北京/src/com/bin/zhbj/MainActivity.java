package com.bin.zhbj;


import com.bin.zhbj.fragment.ContentFragment;
import com.bin.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {
	private static final String FRAGMENT_LEFT_MENU =  "fragment_left_menu";////标记。可以根据标记找到该fragment
	private static final String FRAGMENT_CONTENT = "fragment_content";
	private FragmentManager fm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);// 设置侧边栏
		SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置触摸范围
		//拿到屏幕宽度
		int width=getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*200/320);// 设置预留屏幕的宽度(200个像素，写死了不是很好,设置占屏幕宽度的百分比，注意是整数)
		
		initFragment();
	}
	/**
	 * 初始化fragment,将fragment填充给布局（frameLayout）文件
	 */
	private void initFragment(){
		fm = getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();//开启事务
		
		//用fragment替换frameLayout
		ft.replace(R.id.activity_main, new ContentFragment(),FRAGMENT_CONTENT);//标记。可以根据标记找到该fragment
		ft.replace(R.id.left_menu, new LeftMenuFragment(),FRAGMENT_LEFT_MENU);
		
		ft.commit();//提交事务
	}
	//获取侧边栏LeftMenuFragment
	public LeftMenuFragment getLeftMenuFragment(){
		return (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
	}
	//获取ContentFragment
	public ContentFragment getContentFragment(){
		return (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
	}

	
}
