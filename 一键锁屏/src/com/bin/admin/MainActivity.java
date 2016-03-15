package com.bin.admin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;
	private Button btnLock;
	private Button btnUnstall;
	private Button btnActive;
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件
		
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE); //电源管理器
		
		
		
		btnActive = (Button) findViewById(R.id.activeManager);
		btnActive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activeAdmin();
			}
		});
		btnLock = (Button) findViewById(R.id.lock);
		btnLock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lockScreen();
				finish();
			}
		});
		btnUnstall = (Button) findViewById(R.id.unstall);
		btnUnstall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unInstall();
			}
		});
		
	}

	// 激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
	public void activeAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"正在激活设备管理器...");
		startActivity(intent);
	}

	// 一键锁屏
	public void lockScreen() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			
			mDPM.lockNow();// 立即锁屏
			//mDPM.resetPassword("123456", 0);
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void clearData() {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.wipeData(0);// 清除数据,恢复出厂设置
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void unInstall() {
		mDPM.removeActiveAdmin(mDeviceAdminSample);// 取消激活

		// 卸载程序
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
}
