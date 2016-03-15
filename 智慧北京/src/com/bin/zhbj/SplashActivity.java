package com.bin.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
	RelativeLayout rlRoot;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		rlRoot=(RelativeLayout) findViewById(R.id.rlRoot);
		startAnim(rlRoot);
		
	}
	/**
	 * 开启动画
	 */
	private void startAnim(RelativeLayout Root){
		//动画集合
		AnimationSet set=new AnimationSet(false);
		//旋转动画
		RotateAnimation rotate=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,
				0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(1000);//动画时间
		rotate.setFillAfter(true);//保持动画状态
		
		//缩放动画
		ScaleAnimation scale=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,
				0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(1000);
		scale.setFillAfter(true);
		
		//渐变动画
		AlphaAnimation alpha=new AlphaAnimation(0,1);
		alpha.setDuration(1000);
		alpha.setFillAfter(true);
		
		set.addAnimation(scale);
		set.addAnimation(rotate);
		set.addAnimation(alpha);
		//设置动画监听
		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//动画完成后跳转到下一页
				
				jumpNextPage();
			}
		});
		Root.startAnimation(set);
	}
	/**
	 * 跳转到下一页
	 */
	private void jumpNextPage(){
		//判断之前有没有显示过新手引导页
		
		boolean userGuide=sp.getBoolean("is_user_guide_showed", false);
		if(!userGuide){
			//跳转到新手引导页
			startActivity(new Intent(SplashActivity.this,GuideActivity.class));
			finish();
		}else{
			startActivity(new Intent(SplashActivity.this,MainActivity.class));
			finish();
		}
		
	}

}
