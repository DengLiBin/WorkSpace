package com.itheima.smartimageview;

import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void click(View v){
		//下载图片
		//1.确定网址
		String path = "http://192.168.13.13:8080/dd.jpg";
		//2.找到智能图片查看器对象
		SmartImageView siv = (SmartImageView) findViewById(R.id.iv);
		//3.下载并显示图片
		siv.setImageUrl(path);
	}

}
