package com.bin.c2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	static{
		System.loadLibrary("hello");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	public void click(View c){
		EditText et=(EditText)findViewById(R.id.et);
		int text=Integer.parseInt(et.getText().toString());
		System.out.println("º”√‹√‹¬Î"+encodePass(text));
	}
	public native int encodePass(int pass);
}
