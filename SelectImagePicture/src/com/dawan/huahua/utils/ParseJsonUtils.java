/*
+---------------------------------------------------------------+
| 名称:ParseJson                           
| 版权:zfw                                                        
| 日期:2014-10-11                                           
| 描述:解析JSON              
+---------------------------------------------------------------+
 */
package com.dawan.huahua.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 解析JSON
 */
public class ParseJsonUtils {

	/**
	 * 将map转化为JSON
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map<String, String> hashMap) {
		JSONObject object = null;
		String jsonString = "";
		if (hashMap != null) {
			object = new JSONObject();
			try {
				@SuppressWarnings("rawtypes")
				Iterator it = hashMap.entrySet().iterator();
				while (it.hasNext()) {
					Object okey = it.next();
					String strkey = okey.toString();
					String[] temp = null;
					temp = strkey.split("=");
					object.put(temp[0], hashMap.get(temp[0]).toString()); // 从集合取出数据，放入JSONObject里面
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("将map转化为JSON时出错:", "将map转化为JSON时出错:" + e.toString());
			}
		} else {
			Log.i("map为空！！", "map为空！！");
		}
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Log.i("toJsonByMap的结果:", "toJsonByMap的结果:" + jsonString);
		return jsonString;
	}

	/**
	 * 将json转化为map
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> jsonToMap(String json) {
		Map<String, String> HashMap = new HashMap<String, String>();
		if (json != null && !json.equals("")) {
			try {
				JSONObject item = new JSONObject(json);
				@SuppressWarnings("rawtypes")
				Iterator it = item.keys();

				while (it.hasNext()) {
					String strKey = it.next().toString();
					HashMap.put(strKey, item.getString(strKey));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return HashMap;
	}

	/**
	 * 将JSON字符串转化为ArrayList
	 * 
	 * @param data
	 * @return ArrayList
	 * @throws Exception
	 */
	public static ArrayList<Map<String, String>> jsonToList(String data) {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i);// 每条记录又由几个Object对象组成 
				HashMap<String, String> hMap = new HashMap<String, String>();

				@SuppressWarnings("rawtypes")
				Iterator it = item.keys();

				while (it.hasNext()) {
					String strKey = it.next().toString();
					hMap.put(strKey, item.getString(strKey));

				}
				list.add(hMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("将JSON字符串转化为ArrayList出错");
		}

		return list;
	}

	/**
	 * 将ArrayList转化为JSON
	 * 
	 * @param arrayList
	 * @return
	 */
	public static String arrayListToJson(
			ArrayList<Map<String, String>> arrayList) {
		JSONArray jsonArray = new JSONArray();
		JSONObject object = null;
		String jsonString = "";

		if (arrayList != null) {
			for (Map<String, String> hashMap : arrayList) {
				object = new JSONObject();
				try {
					@SuppressWarnings("rawtypes")
					Iterator it = hashMap.entrySet().iterator();
					while (it.hasNext()) {
						Object okey = it.next();
						String strkey = okey.toString();
						String[] temp = null;
						temp = strkey.split("=");
						object.put(temp[0], hashMap.get(temp[0]).toString()); // 从集合取出数据，放入JSONObject里面
					}
					jsonArray.put(object); // 把JSONObject对象装入jsonArray数组里面
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			Log.i("列表为空！！", "列表为空！！");
		}
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		return jsonString;
	}
}
