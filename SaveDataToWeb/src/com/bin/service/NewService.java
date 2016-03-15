package com.bin.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/**
 * @author Administrator
 *
 */
public class NewService {
	/**
	 *保存数据
	 *@param title 标题
	 *@param timelength 时长 
	 * 
	 */
	public static boolean save(String title, String timelength) {
		String path="http://192.168.2.104:8080/getdatafromAndroid/GetData";
		Map<String,String> params=new HashMap<String,String>();
		params.put("title", title);
		params.put("timelength", timelength);
		try{
			return sendGetRequest(path,params);
		}catch(Exception e){
			e.printStackTrace();
			Log.i("werfw", "sendGetRequest方法异常");
			
		}
		return false;
		
	}
	
	/**
	 * 发送GET请求
	 * @param path 请求路径
	 * @param params 请求参数
	 * @return 请求是否成功
	 * @throws Exception
	 */
	private static boolean sendGetRequest(String path,
			Map<String, String> params) throws Exception  {
		StringBuilder url=new StringBuilder(path);
		url.append("?");
		for(Map.Entry<String, String> entry : params.entrySet()){
			url.append(entry.getKey()).append("=");
			url.append(entry.getValue());
			url.append("&");
		}
		//遍历完成后，url后面会多一个&符号，删除即可
		url.deleteCharAt(url.length()-1);
		Log.i("bingaer", "url是："+url.toString());
		//String uri="https://www.baidu.com";
		HttpURLConnection conn=(HttpURLConnection) new URL(url.toString()).openConnection();
		
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		
		if(conn.getResponseCode()==200){
			return true;
		}else
		{
			return false;
		}
		
	}
	
}
