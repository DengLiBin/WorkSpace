package com.bin.webpic;

import com.bin.service.ImageService;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText pathText;
	private ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pathText=(EditText) this.findViewById(R.id.imagepath);  
        imageView=(ImageView) this.findViewById(R.id.imageView);
        
        Button button =  (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new ButtonListener());
    }
    private final class ButtonListener implements View.OnClickListener{

		public void onClick(View v) {
			String path=pathText.getText().toString();
			try{
				byte[] data=ImageService.getImage(path);
				Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
				imageView.setImageBitmap(bitmap);//œ‘ æÕº∆¨
			}catch(Exception e){
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), R.string.error, 1).show();
			}
			
		}
    	
    }
}