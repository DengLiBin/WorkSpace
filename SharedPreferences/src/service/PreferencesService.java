package service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;
	
	public PreferencesService(Context context) {
		
		this.context = context;
	}

	public void save(String name, Integer age){
		//SharedPreferences:专门用于保存用户设置参数
		SharedPreferences preferences=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
		Editor editor=preferences.edit();
		editor.putString("name", name);
		editor.putInt("age", age);//当前只是保存在内存中的
		editor.commit();//要将数据提交
		
	}
	
	 public Map<String,String>getPreferences(){
		Map<String,String> params=new HashMap<String,String>();
	    SharedPreferences preferences=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);
	    
	    params.put("name",preferences.getString("name",""));
	    params.put("age", String.valueOf(preferences.getInt("age", 0)));
	    return params;
	 }
}
