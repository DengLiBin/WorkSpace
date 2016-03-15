package service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import domain.Person;

public class PersonService {
	private DBOpenHelper dbOpenHelper;
	
	public PersonService(Context context){
		this.dbOpenHelper=new DBOpenHelper(context);
	}
	public void payment(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();//开启事务
		try{
			//要么都执行要么都不执行
			db.execSQL("update person set amount=amount-10 where personid=1");
			db.execSQL("update person set amount=amount+10 where personid=2");
			db.setTransactionSuccessful();//设置事务的标志为True
		}finally{
			db.endTransaction();//结束事务,有两种情况：commit,rollback,
		//事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
		}
	}
	/**
	 * 添加记录
	 * @param person
	 */
	public void save(Person person){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into person(name,phone)values(?,?)",
				new Object[]{person.getName(),person.getPhone()});
		//db.close();关闭数据库
	}
	/**
	 * 删除记录
	 * @param id 记录ID
	 */
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from person where personid=?", new Object[]{id});
	}
	/**
	 * 更新记录
	 * @param person
	 */
	public void update(Person person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update person set name=?,phone=? where personid=?",
				new Object[]{person.getName(), person.getPhone()});
	}
	/**
	 * 查询记录
	 * @param id 记录ID
	 * @return
	 */
	public Person find(Integer id){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person where personid=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));
			
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			return new Person(personid, name, phone);
		}
		cursor.close();
		return null;
	}
	/**
	 * 分页获取记录
	 * @param offset 跳过前面多少条记录
	 * @param maxResult 每页获取多少条记录
	 * @return
	 */
	public List<Person> getScrollData(int offset,int maxResult){
		List<Person> persons = new ArrayList<Person>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from person order by personid asc limit ?,?",
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		while(cursor.moveToNext()){//当前行
			int personid = cursor.getInt(cursor.getColumnIndex("personid"));//personid这一列
			
			String name = cursor.getString(cursor.getColumnIndex("name"));//name这一列
			String phone = cursor.getString(cursor.getColumnIndex("phone"));//phone这一列
			persons.add(new Person(personid, name, phone));
		}
		cursor.close();
		return persons;
	}
	/**
	 * 分页获取记录
	 * @param offset 跳过前面多少条记录
	 * @param maxResult 每页获取多少条记录
	 * @return
	 */
	public Cursor getCursorScrollData(int offset, int maxResult){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select personid as _id,name,phone,amount from person order by personid asc limit ?,?",
				new String[]{String.valueOf(offset), String.valueOf(maxResult)});
		return cursor;
	}
	
	/**
	 * 获取记录总数
	 * @return
	 */
	public long getCount(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from person", null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}
}
