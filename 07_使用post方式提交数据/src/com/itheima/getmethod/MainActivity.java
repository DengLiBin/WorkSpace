package com.itheima.getmethod;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.itheima.htmlviewer.utils.Utils;
import com.itheima.postmethod.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(MainActivity.this, (String)msg.obj, 0).show();
		}
	};
	
	public void click(View v){
		EditText et_name = (EditText) findViewById(R.id.et_name);
		EditText et_pass = (EditText) findViewById(R.id.et_pass);
		
		final String name = et_name.getText().toString();
		final String pass = et_pass.getText().toString();
		
		Thread t = new Thread(){
			@Override
			public void run() {
				//提交的数据需要经过url编码，英文和数字编码后不变
				@SuppressWarnings("deprecation")
				String path = "http://192.168.13.13/Web2/servlet/LoginServlet";
				
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					
					//拼接出要提交的数据的字符串
					String data = "name=" + URLEncoder.encode(name) + "&pass=" + pass;
					//添加post请求的两行属性
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", data.length() + "");
					
					//设置打开输出流
					conn.setDoOutput(true);
					//拿到输出流
					OutputStream os = conn.getOutputStream();
					//使用输出流往服务器提交数据
					os.write(data.getBytes());
					if(conn.getResponseCode() == 200){
						InputStream is = conn.getInputStream();
						String text = Utils.getTextFromStream(is);
						
						Message msg = handler.obtainMessage();
						msg.obj = text;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
		
		
		
	}

}
