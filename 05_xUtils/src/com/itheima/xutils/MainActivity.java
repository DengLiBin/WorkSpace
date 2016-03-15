package com.itheima.xutils;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView tv_failure;
	private TextView tv_progress;
	private ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_failure = (TextView) findViewById(R.id.tv_failure);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		pb = (ProgressBar) findViewById(R.id.pb);
	}

	public void click(View v){
		HttpUtils utils = new HttpUtils();
		String fileName = "QQPlayer.exe";
		//确定下载地址
		String path = "http://192.168.13.13:8080/" + fileName;
		utils.download(path, //下载地址
				"sdcard/QQPlayer.exe", //文件保存路径
				true,//是否支持断点续传
				true, new RequestCallBack<File>() {
					
					//下载成功后调用
					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						Toast.makeText(MainActivity.this, arg0.result.getPath(), 0).show();
						
					}
					
					//下载失败调用
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						tv_failure.setText(arg1);
					}
					
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);
						pb.setMax((int)total);
						pb.setProgress((int)current);
						tv_progress.setText(current * 100 / total + "%");
					}
				});
	}

}
