package my.file;

import my.service.FileService;
import android.test.AndroidTestCase;
import android.util.Log;

public class FileSericeTest extends AndroidTestCase {
	private static final String TAG="FileServiceTest";
	public void testRead()throws Throwable{
		FileService service=new FileService(this.getContext());
		String result=service.read("itcast.txt");
		Log.e(TAG,result);
	}
}
