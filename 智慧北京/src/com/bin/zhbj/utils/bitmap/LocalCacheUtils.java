package com.bin.zhbj.utils.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.bin.zhbj.utils.GetMD5;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 本地缓存
 * @author Administrator
 *
 */
public class LocalCacheUtils {
	//获取本地sd卡路径
	public static final String CACHE_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache_52";
	/**
	 * 从本地sdcard读图片
	 * @param url
	 */
	public Bitmap getBitmapFromLocal(String url){
		try{
			String fileName=GetMD5.MD5(url);
			File file=new File(CACHE_PATH,fileName);
			if(file.exists()){
				Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(file));
				return bitmap;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 向sdcard写图片
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapToLocal(String url,Bitmap bitmap){
		try{
			String fileName=GetMD5.MD5(url);
			File file=new File(CACHE_PATH,fileName);
			File parentFile=file.getParentFile();
			if(!parentFile.exists()){//如果文件夹不存在，创建文件夹
				parentFile.mkdir();
			}
			//将图片保存到本地
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
