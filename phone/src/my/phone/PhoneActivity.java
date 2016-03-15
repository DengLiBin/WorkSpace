package my.phone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneActivity extends Activity {
    /** Called when the activity is first created. */
	EditText mobileText;//文本框
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//显示布局
       //获取到文本框对象
	   mobileText=(EditText) findViewById(R.id.mobileNumber);
        //获取到拨号界面的Button对象
        Button button=(Button) this.findViewById(R.id.button);
        //为Button添加一个点击监听器
        button.setOnClickListener(new ButtonClickListener());
    }
    //创建一个 监听器内部类，复写onClick(View v)方法
    private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//获取文本框中的数据(电话号码)
			String mobileNumber=mobileText.getText().toString();
			//创建意图对象
			Intent intent=new Intent();
			//匹配意图过滤器
			intent.setAction("android.intent.action.CALL");
			//intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("tel:"+mobileNumber));
			//激活Activity
			startActivity(intent);//方法内部会自动为Intent添加类别：android.intent.category.DEFAULT
		}
    	
    }
}