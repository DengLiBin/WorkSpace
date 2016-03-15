package service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "itcast.db", null, 2);//数据库名称，版本号
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {//是在数据库每一次被创建的时候调用的
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE person (personid integer primary key autoincrement, name varchar(20))");
	}

	@Override
	//数据库版本发生变化调用该方法
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE person ADD phone VARCHAR(12) NULL");
	}

}
