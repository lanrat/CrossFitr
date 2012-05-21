package com.vorsk.crossfitr.models;

import android.content.ContentValues;
	
/**
 * Base class for the child DAOs' Row to extend.
 * 
 * This contains all of the global columns and implements them for each
 * method
 */
public class SQLiteRow
{
	// Cols
	public long   _id;
	public int    date_modified;
	public int    date_created;
	
	public SQLiteRow() {}
	public SQLiteRow(ContentValues vals)
	{
		_id             = vals.getAsLong(SQLiteDAO.COL_ID);
		date_modified   = vals.getAsInteger(SQLiteDAO.COL_MDATE);
		date_created    = vals.getAsInteger(SQLiteDAO.COL_CDATE);
	}
	
	public ContentValues toContentValues()
	{
		ContentValues vals = new ContentValues();
		vals.put(SQLiteDAO.COL_ID,    _id);
		vals.put(SQLiteDAO.COL_MDATE, date_modified);
		vals.put(SQLiteDAO.COL_CDATE, date_created);
		return vals;
	}
	
} // END Row