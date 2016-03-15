package com.binl.readcontacts;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 读取联系人信息，用到了ContentResolver
 * @author DengLibin
 *
 */
public class MainActivity extends Activity {
	private ListView lvList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvList=(ListView) findViewById(R.id.lv_list);
		ArrayList<HashMap<String,String>> readContacts=readContacts();
		
		lvList.setAdapter(new SimpleAdapter(this,readContacts,R.layout.contact_list_item,
				new String[]{"name","phone"},new int[]{R.id.tv_name,R.id.tv_phone}));
	}
	private ArrayList<HashMap<String,String>> readContacts(){
		// 首先,从raw_contacts中读取联系人的id("contact_id")
		// 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
		// 然后,根据mimetype来区分哪个是联系人,哪个是电话号码
		Uri rawContactsUri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri=Uri.parse("content://com.android.contacts/data");
		ContentResolver cr=getContentResolver();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor rawContactsCursor=cr.query(rawContactsUri, new String[]{"contact_id"}, null, null, null);
		
		if(rawContactsCursor!=null){
			while(rawContactsCursor.moveToNext()){
				String contactId=rawContactsCursor.getString(0);//0表示contact_id
				//System.out.println(contactId);
				// 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
				Cursor dataCursor = cr.query(dataUri,
						new String[] { "data1", "mimetype" }, "contact_id=?",
						new String[] { contactId }, null);
				if (dataCursor != null){
					HashMap<String, String> map = new HashMap<String, String>();
					while(dataCursor.moveToNext()){
						String data1 = dataCursor.getString(0);//data1,电话号码
						String mimetype = dataCursor.getString(1);//mimetype
						System.out.println(contactId + ";" + data1 + ";"+ mimetype);
						
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							map.put("phone", data1);
						} else if ("vnd.android.cursor.item/name"
								.equals(mimetype)) {
							map.put("name", data1);
						}
					}
					list.add(map);//一个map对应一个联系人（名字和电话）
					dataCursor.close();
					
				}
			}
			rawContactsCursor.close();
			
		}
		return list;
		
	}
}
