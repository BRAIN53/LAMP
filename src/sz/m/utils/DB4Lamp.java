package sz.m.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.slidinglayout3d.LampItem;
import com.example.slidinglayout3d.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

public class DB4Lamp {
	private Context context;
	private long userID;
	private static final String LAMP_TABLE = "LAMP_TABLE";
	private static final String LAMP_TABLE_CREATE = "create table if not exists "
			+ LAMP_TABLE
			+ " ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,customerID NUMERIC, deviceId TEXT, name TEXT, isOn NUMERIC, color NUMERIC,"
			+ "scene TEXT, f1  TEXT, f2 TEXT, f3 TEXT, " + "f4 TEXT)";

	private static SQLiteDatabase db;
	private DatabaseHelper databaseHelper;

	public DB4Lamp(Context _context, long userID) {
		this.context = _context;
		this.userID = userID;
		String[] indexes = new String[0];
		if (db == null) {
			indexes = new String[0];
			databaseHelper = new DatabaseHelper(context, LAMP_TABLE,
					LAMP_TABLE_CREATE, indexes, 1);
			db = databaseHelper.getReadableDatabase();
		}
	}

	public Cursor loadAllRecord() {
		Cursor cur = db.query(LAMP_TABLE, new String[] { "id", "customerID",
				"deviceId", "name", "isOn", "color", "scene", "f1", "f2", "f3",
				"f4" }, "customerID=" + userID, null, null, null, null);
		return cur;
	}

	public long add(LampItem lamp) {
		ContentValues args = new ContentValues();
		args.put("customerID", userID);
		args.put("deviceId", lamp.getDeviceId());
		args.put("name", lamp.getName());
		args.put("isOn", lamp.getIsOn());
		args.put("color", lamp.getColor());
		args.put("scene", lamp.getScene());
		args.put("f1", lamp.getF1());
		args.put("f2", lamp.getF2());
		args.put("f3", lamp.getF3());
		args.put("f4", lamp.getF4());
		return db.insert(LAMP_TABLE, null, args);
	}

	public boolean delete(LampItem lamp) {
		return db.delete(LAMP_TABLE, "deviceId" + "=" + lamp.getDeviceId()
				+ " and customerID=" + userID, null) > 0;
	}

	public void deleteAll() {
		Cursor cur = loadAllRecord();
		LampItem lamp = new LampItem();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			lamp = cursorToLampItem(cur);
			delete(lamp);
			cur.moveToNext();
		}
	}

	public boolean update(LampItem lamp) {
		ContentValues args = new ContentValues();
		args.put("customerID", userID);
		args.put("deviceId", lamp.getDeviceId());
		args.put("name", lamp.getName());
		args.put("isOn", lamp.getIsOn());
		args.put("color", lamp.getColor());
		args.put("scene", lamp.getScene());
		args.put("f1", lamp.getF1());
		args.put("f2", lamp.getF2());
		
		
		args.put("f3", lamp.getF3());
		args.put("f4", lamp.getF4());

		return db.update(LAMP_TABLE, args,
				"deviceId" + "=" + lamp.getDeviceId() + " and customerID="
						+ userID, null) > 0;

	}

	public LampItem getRecordByDeviceID(String deviceId) {
		Cursor cur = db.query(LAMP_TABLE, new String[] { "id", "customerID",
				"deviceId", "name", "isOn", "color", "scene", "f1", "f2", "f3",
				"f4" }, "deviceId=" + deviceId + " and customerID=" + userID,
				null, null, null, null);
		if (cur != null && cur.getCount() >= 1) {
			cur.moveToFirst();
			return cursorToLampItem(cur);
		} else {
			cur.close();
			return null;
		}
	}

	public LampItem cursorToLampItem(Cursor cur) {
		if (cur == null || cur.isClosed())
			return null;

		LampItem record = new LampItem();
		record.setDeviceId(cur.getString(2));
		record.setName(cur.getString(3));
		record.setIsOn(cur.getInt(4));
		record.setColor(cur.getInt(5));
		record.setScene(cur.getString(6));
		record.setF1(cur.getString(7));
		record.setF2(cur.getString(8));
		record.setF3(cur.getString(9));
		record.setF4(cur.getString(10));

		return record;
	}
	
	

}
