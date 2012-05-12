package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public abstract class SQLiteDAO
{
	//// Constants
	
	// Common column names
	public static final String COL_ID    = "_id";
	public static final String COL_NAME  = "name";
	public static final String COL_DESC  = "description";
	public static final String COL_CDATE = "date_created";
	public static final String COL_MDATE = "date_modified";
	
	private static final String DB_NAME = "CrossFitr";
	private static final int DB_VERSION = 1;
	
	// Pre-populated Type IDs
	public static final int TYPE_WOD    = 1;
	public static final int TYPE_GIRL   = 2;
	public static final int TYPE_HERO   = 3;
	public static final int TYPE_CUSTOM = 4;
	
	public static final int SCORE_TIME   = 1;
	public static final int SCORE_REPS   = 2;
	public static final int SCORE_WEIGHT = 3;
	
	// Abstract
	protected final String DB_TABLE;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
	
		// Constants
		
		
		// The creation script should be stored here
		private static final String db_create_file = "db_create.sql";
		
		public DatabaseHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// TODO: Read db_create_file
			db.execSQL("");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int over, int nver)
		{
			// TODO: This
		}
		
	} // END DatabaseHelper
	
	
	//// Private
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	/***   Constructors   ***/
	
	public SQLiteDAO(String table, Context ctx)
	{
		DB_TABLE = table;
		DBHelper = new DatabaseHelper(ctx);
	}
	
	/***   Private   ***/
	
	private void open() throws SQLException
	{
		if (db != null) {
			db = DBHelper.getWritableDatabase();
		}
	}
	
	// TODO: Decide whether to auto-call or force programmer
	private void close()
	{
		DBHelper.close();
		db = null;
	}
	
	/***   Protected   ***/
	
	protected long insert(ContentValues cv)
	{
		Date now = new Date();
		long time = now.getTime(); // Milliseconds
		
		// Automated input for global columns
		cv.put(null, COL_ID);     // Always let this autoincrement
		cv.put(String.valueOf(time), COL_MDATE);
		cv.put(String.valueOf(time), COL_CDATE);
		
		return db.insert(DB_TABLE, null, cv);
	}
	
	protected int update(ContentValues cv, String where)
	{
		if (where == null) return -1; // GTFO. You are not updating everything.
		
		return db.update(DB_TABLE, cv, where, null);
	}
	
	// TODO: Should just make a deleteByID and disable this...
	protected int delete(String where)
	{
		if (where == null) return -1; // ... Really? GTFO x 99999
		
		return db.delete(DB_TABLE, where, null);
	}
	
	protected Cursor select(String[] cols, String[] vals) throws SQLException
	{
		String sql = "SELECT * FROM " + DB_TABLE;
		
		// Build the WHERE clause (append each col-val)
		if (cols.length > 0) {
			sql += " WHERE ";
		}
		for (int ii=0; ii<cols.length; ii++) {
			if (ii != 0) sql += ", ";
			sql += cols[ii] + " = ?";
		}
		
		return db.rawQuery(sql, vals);
	}
	
	protected Cursor selectByID(long id) throws SQLException
	{
		return db.rawQuery(
			"SELECT * FROM " + DB_TABLE + " WHERE id = " + id,
			null
		);
	}
	
	/***   Public   ***/
	
}
