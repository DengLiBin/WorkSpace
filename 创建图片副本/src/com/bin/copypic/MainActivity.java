package com.bin.copypic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //这个对象是只读的
        Bitmap bmSrc = BitmapFactory.decodeFile("sdcard/ice151228-02.jpg");
        
        //创建图片副本
        //1.在内存中创建一个与原图一模一样大小的bitmap对象，创建与原图大小一致的白纸
        Bitmap bmCopy = Bitmap.createBitmap(bmSrc.getWidth(), bmSrc.getHeight(), bmSrc.getConfig());
        
        //2.创建画笔对象
        Paint paint = new Paint();
        
        //3.创建画板对象，把白纸铺在画板上
        Canvas canvas = new Canvas(bmCopy);
        
        //4.开始作画，把原图的内容绘制在白纸上
        Matrix mt=new Matrix();
        //平移
//      mt.setTranslate(20, 40);
      //缩放
      //sx：水平方向的缩放比例
      //sy：竖直方向的缩放比例
//      mt.setScale(0.5f, 0.5f);
//      mt.setScale(0.5f, 0.5f, bmCopy.getWidth() / 2, bmCopy.getHeight() / 2);
      //旋转
//      mt.setRotate(45, bmCopy.getWidth() / 2, bmCopy.getHeight() / 2);
      
      //镜面
//      mt.setScale(-1, 1);
//      mt.postTranslate(bmCopy.getWidth(), 0);
      //倒影
        mt.setScale(1, -1);
        mt.postTranslate(0, bmCopy.getHeight());
        canvas.drawBitmap(bmSrc,mt, paint);
        
        ImageView iv_src = (ImageView) findViewById(R.id.iv_src);
        ImageView iv_copy = (ImageView) findViewById(R.id.iv_copy);
        iv_src.setImageBitmap(bmSrc);
        iv_copy.setImageBitmap(bmCopy);
    }
}
