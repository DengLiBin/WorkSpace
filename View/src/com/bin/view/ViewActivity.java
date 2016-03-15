package com.bin.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ViewActivity extends Activity {
    /** Called when the activity is first created. */
	int count=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button=(Button) findViewById(R.id.button01);
        
        button.setOnClickListener(new onClickListener());
    }
    private final class onClickListener implements View.OnClickListener{

		public void onClick(View v) {
			LinearLayout ll=(LinearLayout) findViewById(R.id.lla);//获取线性布局对象
			String msg=ViewActivity.this.getResources().getString(R.string.button);//获取字符串
			Button tempButton=new Button(ViewActivity.this);//创建一个按钮
			tempButton.setText(msg+(count++));//按钮的显示信息
			tempButton.setWidth(80);//设置按钮的宽度
			ll.addView(tempButton);//将按钮添加到先行布局中
		}
    	
    }
}