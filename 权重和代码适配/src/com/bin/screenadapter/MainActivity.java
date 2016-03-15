package com.bin.screenadapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 代码中设置布局
 * @author Administrator
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//拿到屏幕宽高
		int width=getWindowManager().getDefaultDisplay().getWidth();
		int height=getWindowManager().getDefaultDisplay().getHeight();
		
		TextView tv1=(TextView)findViewById(R.id.tv_tv1);
		TextView tv2=(TextView) findViewById(R.id.tv_tv2);
		
		//因为父控件是LinearLayout,所以导android.widget.LinearLayout这个包
		LayoutParams params=new LayoutParams(width/3,(int) (height*0.2));
		
		tv1.setLayoutParams(params);
		tv2.setLayoutParams(params);
		
		
	}


}
