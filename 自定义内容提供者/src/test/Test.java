package test;

import openhelper.MyOpenHelper;
import android.test.AndroidTestCase;

public class Test extends AndroidTestCase {
	public void test(){
		MyOpenHelper oh=new MyOpenHelper(getContext());
		oh.getWritableDatabase();//创建数据库
	}
}
