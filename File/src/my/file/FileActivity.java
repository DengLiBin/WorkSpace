package my.file;

import my.service.FileService;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FileActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button=(Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonClickListener());
        
    }
    private class ButtonClickListener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			EditText filenameText=(EditText) findViewById(R.id.filename);
			EditText filecontentText=(EditText) findViewById(R.id.filecontent);
			
			String filename=filenameText.getText().toString();
			String content=filecontentText.getText().toString();
			
			FileService service=new FileService(getApplicationContext());
			try{
				service.save(filename,content);//调用保存方法
				//通知用户文件保存完成
				Toast.makeText(getApplicationContext(),R.string.success,1).show();
			}catch(Exception e){
				//通知用户文件保存失败
				Toast.makeText(getApplicationContext(),R.string.fail,1).show();
				e.printStackTrace();
			}
			
			
			
			
		}
    	
    }
}