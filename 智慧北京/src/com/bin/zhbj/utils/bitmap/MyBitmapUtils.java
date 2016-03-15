package com.bin.zhbj.utils.bitmap;

import com.bin.zhbj.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {
	NetCacheUtils mNetCacheUtils;
	LocalCacheUtils mLocalCacheUtils;
	MemoryCacheUtils mMemoryCacheUtils;
	public MyBitmapUtils(){
		mLocalCacheUtils=new LocalCacheUtils();
		mMemoryCacheUtils=new MemoryCacheUtils();
		mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);
	}
	public void display(ImageView ivPic,String url){
		Bitmap bitmap=null;
		ivPic.setImageResource(R.drawable.news_pic_default);//设置默认加载的图片
		//从内存读取
		 bitmap=mMemoryCacheUtils.getBitmapFromMemory(url);
		 if(bitmap!=null){
				ivPic.setImageBitmap(bitmap);
				System.out.println("从内存读取图片");
				return;
			}else{
				System.out.println("内存为空");
			}
		//从本地读取
		 bitmap=mLocalCacheUtils.getBitmapFromLocal(url);
		if(bitmap!=null){
			ivPic.setImageBitmap(bitmap);
			System.out.println("从本地读取图片");
			System.out.println("将图片从本地保存到内存中");
			mMemoryCacheUtils.setBitmapToMemory(url, bitmap);//将图片保存到内存
			if(mMemoryCacheUtils.getBitmapFromMemory(url)==null){
				System.out.println("图片没有保存到内存中");
			}else{
				System.out.println("图片已经保存到内存中");
			}
			return;//从本地读取到图片，就是不用访问网络了
		}
		//从网络读取
		mNetCacheUtils.getBitmapFromNet(ivPic,url);
	}
}
