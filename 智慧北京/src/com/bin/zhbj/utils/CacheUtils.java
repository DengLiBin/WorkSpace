package com.bin.zhbj.utils;

import android.content.Context;

/**
 * @author Administrator
 *缓存工具类
 */
public class CacheUtils {
	/**
	 * 设置缓存
	 * key是json的url
	 * value是json格式的字符串文本文件
	 */
	public static void setCache(Context ctx,String key,String value){
		PrefUtils.setString(ctx,key,value);
		//可以将缓存放入文件中，文件名就是MD5(url)，文件内容json
	}
	
	/**
	 * 读取缓存
	 */
	public static String getCache(Context ctx,String key){
		return PrefUtils.getString(ctx, key,null);
	}
}
