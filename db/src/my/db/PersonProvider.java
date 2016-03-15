package my.db;

import service.DBOpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PersonProvider extends ContentProvider {
	private DBOpenHelper dbOpenHelper;
	private static final UriMatcher MATCHER=new UriMatcher(UriMatcher.NO_MATCH);
	private static final int PERSONS=1;
	static{
		MATCHER.addURI("my.providers.personprovider", "person", PERSONS);
	}
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbOpenHelper=new DBOpenHelper(this.getContext());
		return false;
	}
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
		switch(MATCHER.match(uri)){
		case 1:
			long rowid=db.insert("person", "name", values);//Ö÷¼üÖµ
			Uri insertUri=Uri.parse("content://my.providers.personprovider"+rowid);
			return insertUri;
			
		default :
			throw new IllegalArgumentException("this is Unknown Uri:"+uri);
			
		}
		
	}

	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
