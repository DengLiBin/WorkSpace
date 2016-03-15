package provider;

import openhelper.MyOpenHelper;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 自定义内容提供者，要在清单文件中配置，四大组件：activity,service,receiver,provider
 * @author Administrator
 *
 */
public class PersonProvider extends ContentProvider {

	MyOpenHelper oh;
	SQLiteDatabase db;
	//创建uri匹配器对象
	static UriMatcher um=new UriMatcher(UriMatcher.NO_MATCH);
	//检测其他用户传入的uri与匹配器定义好的uri中，哪条匹配（数据库中有person和teacher表）
	static {
		um.addURI("com.bin.people", "person", 1);//content://com.bin.people/person
		um.addURI("com.bin.people", "teacher", 2);//content://com.bin.people/teacher
		um.addURI("com.bin.people", "person/#", 3);//content://com.bin.people/person/4
		// #表示数字，数字是返回码
	}
	//内容提供者创建时（其他应用同过内容提供者访问数据时）调用
	@Override
	public boolean onCreate() {
		oh=new MyOpenHelper(getContext());
		db=oh.getWritableDatabase();//创建数据库
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		String[] selectionArgs, String sortOrder) {
		Cursor cursor=null;
				db.query("person", projection, selection, selectionArgs, null, null, sortOrder,null);
		if(um.match(uri)==1){
			cursor=db.query("person", projection, selection, selectionArgs, null, null, sortOrder,null);
		}
		if(um.match(uri)==2){
			cursor=db.query("teacher", projection, selection, selectionArgs, null, null, sortOrder,null);
		}
		if(um.match(uri)==3){
			long id=ContentUris.parseId(uri);//把URI末尾携带的数字取出来
			cursor=db.query("person",projection,"_id=?",new String[]{id+""},null,null,sortOrder,null);
		}
		return cursor;//查询数据库返回的结果就是Cursor类型
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		if(um.match(uri) == 1){//请求多条数据
			return "vnd.android.cursor.dir/person";
		}
		else if(um.match(uri) == 3){//请求单条数据
			return "vnd.android.cursor.item/person";
		}
		return null;
		
	}

	//此方法供其他应用调用，用于往数据库插入数据
	//values:由其他应用传入，用于封装要插入的数据
	//URI:内容提供者的主机名，也就是地址(在清单文件中配置)
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//db.insert("person", null, values);
		//使用uri匹配器匹配传入的uri，根据定义好的返回码，就知道要往哪个表插入数据
		if(um.match(uri) == 1){
			db.insert("person", null, values);
			
			//发送数据改变的通知
			//uri:通知发送到哪一个uri上，所有注册在这个uri上的内容观察者都可以收到这个通知
			getContext().getContentResolver().notifyChange(uri, null);
		}
		else if(um.match(uri) == 2){
			db.insert("teacher", null, values);
			
			getContext().getContentResolver().notifyChange(uri, null);
		}
		else{
			throw new IllegalArgumentException("uri有问题哟亲么么哒");
		}
		return uri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int i=db.delete("person",selection,selectionArgs);//返回值为删除多少 行
		return i;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int i=db.update("person", values, selection, selectionArgs);
		return i;
	}

}
