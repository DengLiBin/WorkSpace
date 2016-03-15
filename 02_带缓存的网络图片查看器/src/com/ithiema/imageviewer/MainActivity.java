package com.ithiema.imageviewer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.ithiema.cacheimageviewer.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static ImageView iv;
	static MainActivity ma;
	static Handler handler = new Handler(){
		//此方法在主线程中调用，可以用来刷新ui
		public void handleMessage(android.os.Message msg) {
			//处理消息时，需要知道到底是成功的消息，还是失败的消息
			switch (msg.what) {
			case 1:
				//把位图对象显示至imageview
				iv.setImageBitmap((Bitmap)msg.obj);
				break;

			case 0:
				Toast.makeText(ma, "请求失败", 0).show();
				break;
			}
			
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv = (ImageView) findViewById(R.id.iv);
		ma = this;
	}

	public void click(View v){
		//下载图片
		//1.确定网址
		final String path = "http://192.168.13.13:8080/dd.jpg";
		final File file = new File(getCacheDir(), getFileName(path));
		//判断，缓存中是否存在该文件
		if(file.exists()){
			//如果缓存存在，从缓存读取图片
			System.out.println("从缓存读取的");
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			iv.setImageBitmap(bm);
		}
		else{
			//如果缓存不存在，从网络下载
			System.out.println("从网上下载的");
			Thread t = new Thread(){
				@Override
				public void run() {
					
					try {
						//2.把网址封装成一个url对象
						URL url = new URL(path);
						//3.获取客户端和服务器的连接对象，此时还没有建立连接
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						//4.对连接对象进行初始化
						//设置请求方法，注意大写
						conn.setRequestMethod("GET");
						//设置连接超时
						conn.setConnectTimeout(5000);
						//设置读取超时
						conn.setReadTimeout(5000);
						//5.发送请求，与服务器建立连接
						conn.connect();
						//如果响应码为200，说明请求成功
						if(conn.getResponseCode() == 200){
							//获取服务器响应头中的流，流里的数据就是客户端请求的数据
							InputStream is = conn.getInputStream();
							
							//读取服务器返回的流里的数据，把数据写到本地文件，缓存起来
							
							FileOutputStream fos = new FileOutputStream(file);
							byte[] b = new byte[1024];
							int len = 0;
							while((len = is.read(b)) != -1){
								fos.write(b, 0, len);
							}
							fos.close();
							
							//读取出流里的数据，并构造成位图对象
							//流里已经没有数据了
//							Bitmap bm = BitmapFactory.decodeStream(is);
							Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
							
							
							Message msg = new Message();
							//消息对象可以携带数据
							msg.obj = bm;
							msg.what = 1;
							//把消息发送至主线程的消息队列
							handler.sendMessage(msg);
							
						}
						else{
//							Toast.makeText(MainActivity.this, "请求失败", 0).show();
							
							Message msg = handler.obtainMessage();
							msg.what = 0;
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
	
	public String getFileName(String path){
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}

}
