package my.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Person;

import service.PersonService;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class DbActivity extends Activity {
	private ListView listView;
	private PersonService personService;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        personService=new PersonService(this);
        
        listView=(ListView) this.findViewById(R.id.listView);
     /*  
        try {
			personTest.testSave();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        show2();//显示数据
    }
	private void show() {
		// TODO Auto-generated method stub
		List<Person> persons=personService.getScrollData(0, 10);
		List<HashMap<String,Object>> data=new ArrayList<HashMap<String,Object>>();
		
		for(Person person:persons){
			HashMap<String,Object> item=new HashMap<String, Object>();
			item.put("name", person.getName());
			item.put("phone", person.getPhone());
			item.put("id", person.getPersonid());
			
			data.add(item);//将map集合放入到List集合中
		} 
		//适配器
		SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.item,
							new String[]{"name","phone"},//map集合中的KEY
							new int[]{R.id.name,R.id.phone});//要 绑定的控件
		listView.setAdapter(adapter);
	}
	private void show2() {
		Cursor cursor = personService.getCursorScrollData(0, 20);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor,
				new String[]{"name", "phone"}, new int[]{R.id.name, R.id.phone});
		listView.setAdapter(adapter);
	}
}