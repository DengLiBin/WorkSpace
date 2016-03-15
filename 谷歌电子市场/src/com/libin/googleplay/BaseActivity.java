package com.libin.googleplay;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BaseActivity extends  ActionBarActivity {
	// 管理运行的所有的activity
	public final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		synchronized (mActivities) {
			mActivities.add(this);
		}
		init();
		initView();
		initActionBar();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		synchronized (mActivities) {
			mActivities.remove(this);
		}	
	}
	
	public void killAll() {
		// 复制了一份mActivities 集合
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new LinkedList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
		// 杀死当前的进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	protected void initActionBar() {
	}
	protected void initView() {
	}
	protected void init() {
	}
	
}
