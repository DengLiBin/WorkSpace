package com.example.visitcontentprovider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void insert(View v){
    	//通过内容提供者，把数据插入到“自定义内容提供者项目”的people数据库
    	//拿到contentResolver
    	ContentResolver cr=getContentResolver();
    	ContentValues values=new ContentValues();
    	for(int i=1;i<=20;i++){
    		values.put("name", "赵流"+i);
//        	values.put("money","13000"+i*100);
 //       	cr.insert(Uri.parse("content://com.bin.people"), values);//会调用内容提供者的insert方法
    		//往数据库的teacher表中插入数据
            cr.insert(Uri.parse("content://com.bin.people/teacher"), values);//会调用内容提供者的insert方法(先进行uri匹配)
        	
    	}
   
     }	
    public void delete(View v){
    	ContentResolver cr=this.getContentResolver();
    	ContentValues values=new ContentValues();
    	int i=cr.delete(Uri.parse("content://com.bin.people"), "name=?",new String[]{"赵帅哥"});
    	System.out.println("删除 了"+i+"行");
    }
    public void update(View v){
    	ContentResolver cr=this.getContentResolver();
    	ContentValues values=new ContentValues();
    	values.put("name", "sb志");
    	int i=cr.update(Uri.parse("content://com.bin.people"), values, "name=?", new String[]{"赵流4"});
    	System.out.println("修改了"+i+"行");
    }
    public void query(View v){
    	ContentResolver cr=this.getContentResolver();
    //	Cursor cursor=cr.query(Uri.parse("content://com.bin.people"), null,null,null,null);
    //	Cursor cursor=cr.query(Uri.parse("content://com.bin.people//teacher"), null,null,null,null);
    	Cursor cursor=cr.query(Uri.parse("content://com.bin.people//person/4"), null,null,null,null);//可以通过URI传递数据（4）
    	while(cursor.moveToNext()){
    		String name=cursor.getString(1);
    		String money=cursor.getString(2);
    		System.out.println(name+"---"+money);
    	}
    	
    }
}
