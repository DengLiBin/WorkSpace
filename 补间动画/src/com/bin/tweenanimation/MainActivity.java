package com.bin.tweenanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private ImageView iv;
	private RotateAnimation ra;//旋转动画
	private TranslateAnimation ta;//位移动画
	private AlphaAnimation aa;//渐变动画
	private ScaleAnimation sa;//缩放动画
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv=(ImageView) findViewById(R.id.iv);
	}
	public void translate(View v){
		//ta=new TranslateAnimation(10,100,10,100);
		ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 2, 
				Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 1.5f);
		//设置播放时间
		ta.setDuration(2000);
		//设置重复次数
		ta.setRepeatCount(1);
		ta.setRepeatMode(Animation.REVERSE);
		iv.startAnimation(ta);
	}
	public void scale(View v){
//		sa = new ScaleAnimation(fromX, toX, fromY, toY, iv.getWidth() / 2, iv.getHeight() / 2);
		sa = new ScaleAnimation(0.5f, 2, 0.1f, 3, Animation.RELATIVE_TO_SELF, 0.5f, 
													Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(2000);
		//填充动画的结束位置
		sa.setRepeatCount(1);
		sa.setRepeatMode(Animation.REVERSE);
		sa.setFillAfter(true);
		iv.startAnimation(sa);
	}
	
	public void alpha(View v){
		aa = new AlphaAnimation(0, 1);
		aa.setDuration(2000);
		sa.setRepeatCount(1);
		iv.startAnimation(aa);
	}

	public void rotate(View v){
		ra = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, 
										Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(2000);
		ra.setRepeatCount(1);
		ra.setRepeatMode(Animation.REVERSE);
		iv.startAnimation(ra);
	}
	
	public void fly(View v){
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(ta);
		set.addAnimation(sa);
		set.addAnimation(ra);
		set.addAnimation(aa);
		
		iv.startAnimation(set);
	}
}
