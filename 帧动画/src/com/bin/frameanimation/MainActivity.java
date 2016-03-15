package com.bin.frameanimation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

	//动画实现的原理：y=at+b（也可以是其他对应关系）,t:时间，y位置。位置随时间变化就是动画，
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView iv=(ImageView) this.findViewById(R.id.iv);
		//把帧动画的资源文件指定为iv的背景
		iv.setBackgroundResource(R.drawable.frameanimation);
		//获取iv的背景
		AnimationDrawable ad=(AnimationDrawable) iv.getBackground();
		ad.start();
	}
}
