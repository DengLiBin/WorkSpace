package com.bin.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends Activity {

	private DrawerLayout drawer;
	private RotateAnimation ra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawer = (DrawerLayout) findViewById(R.id.drawer);
		drawer.openDrawer(Gravity.LEFT);//默认打开左边抽屉，左边不需要有布局，不然会报错
		drawer.setAlpha(0.3f);//透明度
		ra = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
				ra.setDuration(2000);
				ra.setRepeatCount(1);
				ra.setRepeatMode(Animation.REVERSE);
		drawer.setAnimation(ra);//动画效果
		
		drawer.setDrawerListener(new DrawerListener(){//给抽屉设置一个监听器

			@Override
			public void onDrawerClosed(View arg0) {
				
			}

			@Override
			public void onDrawerOpened(View arg0) {
				
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				
			}

			@Override
			public void onDrawerStateChanged(int arg0) {
				
			}
			
		});
	}

	
}
