package my.junitest;

import junit.framework.Assert;
import my.service.PersonService;
import android.test.AndroidTestCase;

public class PersonServiceTest extends AndroidTestCase {
	
	public void testSave()throws Exception{
		PersonService service=new PersonService();
		service.save(null);
	}
	public void testAdd()throws Exception{
		PersonService service=new PersonService();
		int actual=service.add(1,2);
		Assert.assertEquals(3, actual);//测试结果是否为3
	}
}
