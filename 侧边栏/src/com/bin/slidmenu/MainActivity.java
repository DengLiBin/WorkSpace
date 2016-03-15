package com.bin.slidmenu;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 添加slidingmenu_library库，并将该库的v4包复制过来替换原来的v4包
 * @author Administrator
 *
 */
public class MainActivity extends SlidingFragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);//设置侧边栏布局
		
		SlidingMenu slidingMenu=getSlidingMenu();//获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
		slidingMenu.setSecondaryMenu(R.layout.right_menu);//设置右侧边栏
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);//设置模式，左右都有侧边栏
		
		slidingMenu.setBehindOffset(100);// 设置预留屏幕的宽度
		
	}
	
}