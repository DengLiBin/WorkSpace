package com.bin.rocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
/**
 * 烟雾背景
 * @author Kevin
 *
 */
public class BackgroundActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_bg);

		ImageView ivTop = (ImageView) findViewById(R.id.iv_top);
		ImageView ivBottom = (ImageView) findViewById(R.id.iv_bottom);

		// 渐变动画
		AlphaAnimation anim = new AlphaAnimation(0, 1);
		anim.setDuration(1000);
		anim.setFillAfter(true);// 动画结束后保持状态

		// 运行动画
		ivTop.startAnimation(anim);
		ivBottom.startAnimation(anim);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 1000);// 延时1秒后结束activity
	}

}