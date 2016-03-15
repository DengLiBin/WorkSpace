package com.example.getmsg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import domain.Message;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;


/**
 * @author Administrator
 *
 */

public class MainActivity extends Activity {
	List<Message> smsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //通过内容提供者获取系统短信内容
    public void click(View v){
    	ContentResolver cr=this.getContentResolver();
    	Cursor cursor = cr.query(Uri.parse("content://sms"), new String[]{"address", "date", "body", "type"}, 
    			null, null, null);
    	while(cursor.moveToNext()){
    		String address=cursor.getString(0);
    		long date = cursor.getLong(1);
    		String body = cursor.getString(2);
    		String type = cursor.getString(3);
    		System.out.println(body);
    		Message sms=new Message(body,type,address,date);
    		smsList.add(sms);
    	}
    }
    //备份信息,先要调用获取短信方法click
    public void click2(View v){
    	XmlSerializer xs=Xml.newSerializer();
    	File file=new File("sdcard/sms.xml");
    	FileOutputStream fos;
    	try{
    		fos=new FileOutputStream(file);
    		xs.setOutput(fos, "utf-8");
    		xs.startDocument("utf-8",true);
    		xs.startTag(null, "message");
    		for(Message sms: smsList){
    			xs.startTag(null, "sms");
    			xs.startTag(null, "body");
    			xs.text(sms.getBody());
    			xs.endTag(null, "body");
    			
    			xs.startTag(null, "type");
				xs.text(sms.getType());
				xs.endTag(null, "type");
				
				xs.startTag(null, "address");
				xs.text(sms.getAddress());
				xs.endTag(null, "address");
				
				xs.startTag(null, "date");
				xs.text(sms.getDate() + "");
				xs.endTag(null, "date");
				
				xs.endTag(null, "sms");
				
				
    		}
    		xs.endTag(null, "message");
    		xs.endDocument();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    //插入信息
    public void click3(View v){
    	Thread t = new Thread(){
    		@Override
    		public void run() {
    			try {
					sleep(7500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			ContentResolver cr = getContentResolver();
    	    	ContentValues values = new ContentValues();
    	    	values.put("address", 95555);
    	    	values.put("type", 1);
    	    	values.put("date", System.currentTimeMillis());
    	    	values.put("body", "您尾号为XXXX的信用卡收到1,000,000RMB转账，请注意查收");
    	    	cr.insert(Uri.parse("content://sms"), values);
    		}
    	};
    	t.start();
    }
}
