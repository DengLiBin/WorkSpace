package com.itheima.ipdialer;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View v){
    	EditText et = (EditText) findViewById(R.id.et);
    	SharedPreferences sp = getSharedPreferences("ip", MODE_PRIVATE);
    	sp.edit().putString("ipNumber", et.getText().toString()).commit();
    }
    
}
