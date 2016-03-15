package com.libin.googleplay;

import com.libin.googleplay.R;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DetailActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		ActionBar actionBar=getSupportActionBar();
		//actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);//actionBar显示左上角按钮
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   if(item.getItemId()==android.R.id.home);{//左上角按钮固定写法
		   finish();//结束当前Activity，返回上一个Activity
		   
		   //也可以在清单文件中为该Activity配置一个:android:parentActivityName="com.libingoogelplay.MainActivity" >
		   //属性，当点击actiionBar的返回按钮时，返回上一个activity，这样可以不用复写该方法
		   /*
		    * 要兼容低版本Android还需配置：
		    *  <meta-data
            android:name="android.support.PARENT_ACTIVITY"
            android:value="com.example.myfirstapp.MainActivity" />
		    */
	   }
	   return super.onOptionsItemSelected(item);
	}
	
}
