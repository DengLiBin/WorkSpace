package com.bin.broadcast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void click(View v){
    	EditText et=(EditText) this.findViewById(R.id.et);
    	String ipNumber=et.getText().toString();
    	SharedPreferences sp=this.getSharedPreferences("ip",MODE_PRIVATE );
    	Editor edit=sp.edit();
    	//将ipNumber写入到ip.xml文件中
    	edit.putString("ipNumber", ipNumber);
    	edit.commit();
    	Toast.makeText(getApplicationContext(), "保存成功",1 ).show();
    }
}