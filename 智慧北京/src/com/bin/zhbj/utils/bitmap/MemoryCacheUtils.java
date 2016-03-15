package com.bin.zhbj.utils.bitmap;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存，内存中放入的对象太多，会造成内存溢出
 * @author Administrator
 *
 */
public class MemoryCacheUtils {
	//private HashMap<String,Bitmap> hashMap=new HashMap<String,Bitmap>();
	/* LruCache:least recentlly use 最少最近使用算法
	会将内存控制在一定的大小内, 超出最大值时会自动回收, 这个最大值开发者自己定
	*/
	private LruCache<String,Bitmap> lruCache;//与HashMap相似
	
	public MemoryCacheUtils(){
		long maxMemory=Runtime.getRuntime().maxMemory();//Android默认给每个app只分配16M的内存
		lruCache=new LruCache<String,Bitmap>((int)maxMemory/8){//设置 占用内存大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int byteCount=value.getByteCount();// return getRowBytes() * getHeight();
				//int byteCount=value.getRowBytes()*value.getHeight();//获取图片占用内存大小
				return byteCount;
			}
		};
	}
	/**
	 * 从内存读取
	 */
	public Bitmap getBitmapFromMemory(String url){
		return lruCache.get(url);
	}
	/**
	 * 写入内存
	 */
	public void setBitmapToMemory(String url, Bitmap bitmap){
		lruCache.put(url, bitmap);
	}
}
