package com.vorsk.crossfitr.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TempDB_Darren extends SQLiteOpenHelper {

	public TempDB_Darren(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE today(_id INTEGER PRIMARY KEY AUTOINCREMENT , title TEXT , date TEXT, time TEXT,memo TEXT);");
		db.execSQL("INSERT INTO today VALUES(null, 'title', '2011/7/18','12:00','memo');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int arg2) {
		// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS today");
			onCreate(db);
	}

}
