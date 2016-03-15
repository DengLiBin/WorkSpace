package com.bin.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Fragment03 fg3;
	Fragment02 fg2;
	private EditText et_main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_main=(EditText) this.findViewById(R.id.aet);
        fg3 = new Fragment03();
    	//获取fragment管理器
    	FragmentManager fm = getFragmentManager();
    	//打开事务
    	FragmentTransaction ft = fm.beginTransaction();
    	//把内容显示至帧布局
    	ft.replace(R.id.fl, fg3);
    	//提交
    	ft.commit();
	}
	public void click1(View v){
		//把fragment01的界面显示到帧布局中
		//创建fragment对象
		Fragment01 fg1=new Fragment01();
		//获取fragment管理器
		FragmentManager fm=this.getFragmentManager();
		//打开事务
		FragmentTransaction ft=fm.beginTransaction();
		//把内容显示到帧布局
		ft.replace(R.id.fl, fg1);
		//提交
		ft.commit();
	}
	 public void click2(View v){
	    	//把fragment01的界面显示至帧布局中
	    	//创建fragment对象
	    	fg2 = new Fragment02();
	    	//获取fragment管理器
	    	FragmentManager fm = getFragmentManager();
	    	//打开事务
	    	FragmentTransaction ft = fm.beginTransaction();
	    	//把内容显示至帧布局
	    	ft.replace(R.id.fl, fg2);
	    	//提交
	    	ft.commit();
	    }
	    
	    public void click3(View v){
	    	//把fragment01的界面显示至帧布局中
	    	//获取fragment管理器
	    	FragmentManager fm = getFragmentManager();
	    	//打开事务
	    	FragmentTransaction ft = fm.beginTransaction();
	    	//把内容显示至帧布局
	    	ft.replace(R.id.fl, fg3);
	    	//提交
	    	ft.commit();
	    }
	    //Activity传递数据到fragment，调用Fragment的setText()方法（自定义的方法）
	    public void click4(View v){
	    	//获取数据
	    	String text=et_main.getText().toString();
	    	//传递数据
	    	fg2.setText(text);
	    }
	    public void setText(String text){
	    	et_main.setText(text);
	    }
	
}
