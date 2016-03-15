package com.bin.youkumenu;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class AnimUtils {
	public static int animCount = 0;//记录当前执行的动画数量
	public static void closeMenu(RelativeLayout rl,int startOffset){
		//关闭菜单后，将所有的子view设为不可用
		for(int i=0;i<rl.getChildCount();i++){
			rl.getChildAt(i).setEnabled(false);
		}
		//旋转动画
		RotateAnimation animation=new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,
				0.5f,RotateAnimation.RELATIVE_TO_SELF,1);
		animation.setDuration(500);//持续时间
		animation.setFillAfter(true);//动画结束后保持当时的状态
		animation.setStartOffset(startOffset);//延迟执行，毫秒
		animation.setAnimationListener(new MyAnimationListener());
		rl.startAnimation(animation);
		
	}
	public static void showMenu(RelativeLayout rl,int startOffset){
		//打开菜单后将所有的子菜单设为可用
		for(int i=0;i<rl.getChildCount();i++){
			rl.getChildAt(i).setEnabled(true);
		}
		//旋转动画
		RotateAnimation animation=new RotateAnimation(-180,0,RotateAnimation.RELATIVE_TO_SELF,
				0.5f,RotateAnimation.RELATIVE_TO_SELF,1);
		animation.setDuration(500);//持续时间
		animation.setFillAfter(true);//动画结束后保持当时的状态
		animation.setStartOffset(startOffset);//延迟执行，毫秒
		animation.setAnimationListener(new MyAnimationListener());
		rl.startAnimation(animation);
		
	}
	static class MyAnimationListener implements AnimationListener{
		@Override
		public void onAnimationStart(Animation animation) {
			animCount++;
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			animCount--;
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
	}
}
