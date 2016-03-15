package com.bin.loadpic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void click(View v){
    	//解析图片时需要使用到的参数都封装在这个对象里了
    	Options opt = new Options();
    	//不为像素申请内存，只获取图片宽高
    	opt.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile("sdcard/dog.jpg", opt);
    	//拿到图片宽高
    	int imageWidth = opt.outWidth;
    	int imageHeight = opt.outHeight;
    	
    	Display dp = getWindowManager().getDefaultDisplay();
    	//拿到屏幕宽高
		int screenWidth = dp.getWidth();
    	int screenHeight = dp.getHeight();
    	
    	//计算缩放比例
    	int scale = 1;
    	int scaleWidth = imageWidth / screenWidth;
    	int scaleHeight = imageHeight / screenHeight;
    	if(scaleWidth >= scaleHeight && scaleWidth >= 1){
    		scale = scaleWidth;
    	}
    	else if(scaleWidth < scaleHeight && scaleHeight >= 1){
    		scale = scaleHeight;
    	}
    	
    	//设置缩放比例
    	opt.inSampleSize = scale;
    	opt.inJustDecodeBounds = false;
    	Bitmap bm = BitmapFactory.decodeFile("sdcard/dog.jpg", opt);
    	
    	ImageView iv = (ImageView) findViewById(R.id.iv);
    	iv.setImageBitmap(bm);
    }
}
