package my.junitest;

import android.test.AndroidTestCase;
import android.util.Log;

public class LogTest extends AndroidTestCase {
	private static final String TAG="LogTest";
	public void testOutLog()throws Throwable{
		Log.i(TAG, "www.itcast.cn");
		Log.e(TAG,"itcast.cn");//输出错误日志信息
		Log.w(TAG,"www.cn" );//输出警告日志信息
	}

}
