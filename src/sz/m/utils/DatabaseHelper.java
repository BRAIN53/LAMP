package sz.m.utils;

import java.util.Date;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private String mCreateDB = "";
	private String[] mIndexes = null;
	private String mDBName = "";
	public DatabaseHelper (Context context, String dbName, String createDB, String[] indexes, int version)
	{
		super(context, dbName, null, version);
		mCreateDB=createDB;
		mDBName = dbName;
		mIndexes = indexes;
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(mCreateDB);
		if(mIndexes!=null)
		{
			for(int i=0;i<mIndexes.length;i++)
			{
				db.execSQL(mIndexes[i]);
			}
		}
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) 
	{
		Log.w("DatabaseHelper", "Upgrading database from version " + oldVersion 
				+ " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+mDBName);
		onCreate(db);
	}
	
	public SQLiteDatabase open() throws SQLException 
	{
		return this.getWritableDatabase();
	}

	/*
	 * Convert value to secure string.
	 * */
	public static String ToSecureString(String value)
	{
		if(value==null || value.equals(""))
			return value;

		value = value.replace("'", "''");
		
		return value;
	}
	
	/*Convert date to time stamp.
	 * */
	public static Long TimeToLong(Date date)
	{
		if(date==null)
			return 0L;
		
		return date.getTime();
	}
	
	/*
	 * Convert long time stamp to date.
	 * */
	public static Date LongTotime(Long lDate)
	{
		if(lDate==0)
			return new Date(1970,1,1);
		else
			return new Date(lDate);
	}
}
	
	