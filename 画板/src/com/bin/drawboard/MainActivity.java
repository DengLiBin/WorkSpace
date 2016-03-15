package com.bin.drawboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {
	private ImageView iv;

	int startX;
	int startY;

	private Canvas canvas;

	private Paint paint;

	private Bitmap bmCopy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//加载画画板的背景图
		Bitmap bmSrc = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		//在内存中创建一个与背景图一模一样大小的bitmap对象，创建与原图大小一致的白纸
		bmCopy = Bitmap.createBitmap(bmSrc.getWidth(), bmSrc.getHeight(), bmSrc.getConfig());
		paint = new Paint();
		canvas = new Canvas(bmCopy);
		//绘制（背景副本）
		canvas.drawBitmap(bmSrc, new Matrix(), paint);
		
		iv = (ImageView) findViewById(R.id.iv);
		iv.setImageBitmap(bmCopy);
		
		//设置触摸侦听
		iv.setOnTouchListener(new OnTouchListener() {
			
			//触摸屏幕时，触摸事件产生时，此方法调用
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				//用户手指摸到屏幕
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					break;
				//用户手指正在滑动
				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getX();
					int y = (int) event.getY();
					//在两个坐标之间画一条线
					canvas.drawLine(startX, startY, x, y, paint);
					//每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
					startX = x;
					startY = y;
					iv.setImageBitmap(bmCopy);
					break;
				//用户手指离开屏幕
				case MotionEvent.ACTION_UP:
					break;

				}
				//true：告诉系统，这个触摸事件由我来处理
				//false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给ImageView的父节点 RelativeLayout
				return true;
			}
		});
			
		
	}

	public void red(View v){
		paint.setColor(Color.RED);
	}
	public void green(View v){
		paint.setColor(Color.GREEN);
	}
	public void brush(View v){
		paint.setStrokeWidth(7);
	}
	public void save(View v){
		File file = new File("sdcard/dazuo.png");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmCopy.compress(CompressFormat.PNG, 100, fos);//100表示压缩比例（0-100）
		/*
		保存图片
		SD每次准备的时候，系统其实是遍历sd卡所有文件，系统会把所有的多媒体文件，都在MediaStore数据库中生成一个索引，数据库中保存了文件的文件名、路径、大小、长度和艺术家
		图库、音乐、视频程序每次启动时，其实不会去遍历sd卡寻找多媒体文件，而是直接从MediaStore数据库中读取多媒体文件，通过库中的索引找到对应的多媒体文件后，把文件显示在界面
		*/
		//发送sd卡就绪广播(遍历Sdcard)
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
		intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
		sendBroadcast(intent);
	}
}
