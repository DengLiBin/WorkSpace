package com.bin.savedata;

import com.bin.service.NewService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SaveDataToWebActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText titleText;
	private EditText lengthText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        titleText=(EditText) this.findViewById(R.id.title);
        lengthText=(EditText) this.findViewById(R.id.timelength);
    }
    public void save(View v){
    	String title=titleText.getText().toString();
    	String timelength=lengthText.getText().toString();
    	boolean result=NewService.save(title,timelength);
    	if(result){
    		Toast.makeText(getApplicationContext(), R.string.success, 1).show();
    	}else{
    		Toast.makeText(this.getApplicationContext(), R.string.fail, 1).show();
    	}
    }
}