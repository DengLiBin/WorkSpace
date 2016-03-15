package com.bin.c;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	static{
		//加载打包完毕的so类库
		System.loadLibrary("hello");//类库名称，在Anadroid.mk文件中指定
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	public void click(View v){
		Toast.makeText(this, helloFromC(), 0).show();
	}
	
	public void click2(View v){
		Toast.makeText(this, "3+5的和为："+add(3,5), 0).show();
	}
	 //定义一个本地方法，方法体由c语言实现
	public native String helloFromC();
	
	public native int add(int i, int j);
}
