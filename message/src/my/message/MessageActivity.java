package my.message;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText numberText;
	private EditText messageText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        numberText=(EditText) this.findViewById(R.id.number);
        messageText=(EditText) this.findViewById(R.id.message);
        Button button=(Button) this.findViewById(R.id.button);
        
        button.setOnClickListener(new ButtonClickListener());
    }
    private final class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			
			String number=numberText.getText().toString();
			String message=messageText.getText().toString();
			//获取短信管理器对象
			SmsManager manager=SmsManager.getDefault();
			//短信字数太多自动拆分成多条短信发送
			ArrayList<String> texts=manager.divideMessage(message);
			for(String text: texts){
				manager.sendTextMessage(number, null, text, null, null);
			}
			//通知用户短信发送成功（采用Toast方式）
			Toast.makeText(MessageActivity.this,R.string.success,Toast.LENGTH_LONG).show();
			
		}
    	
    }
    
}