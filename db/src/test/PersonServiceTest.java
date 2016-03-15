package test;

import java.util.List;

import domain.Person;
import service.DBOpenHelper;
import service.PersonService;
import android.test.AndroidTestCase;
import android.util.Log;

public class PersonServiceTest extends AndroidTestCase {
	private static final String TAG = "PersonServiceTest";
	public void testCreateDB() throws Exception{
		DBOpenHelper dbOpenHelper=new DBOpenHelper(getContext());
		dbOpenHelper.getWritableDatabase();
	}
	public void testSave() throws Exception{
		PersonService service = new PersonService(this.getContext());
		for(int i = 0 ; i < 20 ; i++){
			Person person = new Person(100+ i, "zhangxx"+i, "234233"+i );
			service.save(person);
		}
	}
	
	public void testDelete() throws Exception{
		PersonService service = new PersonService(this.getContext());
		service.delete(21);
	}
	
	public void testUpdate() throws Exception{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		person.setName("zhangxiaoxiao");
		service.update(person);
	}
	
	public void testFind() throws Exception{
		PersonService service = new PersonService(this.getContext());
		Person person = service.find(1);
		Log.i(TAG, person.toString());
	}
	
	public void testScrollData() throws Exception{
		PersonService service = new PersonService(this.getContext());
		List<Person> persons = service.getScrollData(0, 5);
		for(Person person : persons){
			Log.i(TAG, person.toString());
		}
	}
	
	public void testCount() throws Exception{
		PersonService service = new PersonService(this.getContext());
		long result = service.getCount();
		Log.i(TAG, result+"");
	}
}
