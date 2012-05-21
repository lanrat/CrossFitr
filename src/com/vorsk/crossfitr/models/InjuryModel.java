package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "injury" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of InjuryRow where each column
 * is a publicly accessible property.
 * 
 * @author Vivek
 * @since 1.0
 */
public class InjuryModel extends SQLiteDAO
{
	//// Constants
	
	// Table-specific columns
	public static final String COL_DESC  = "description";
	public static final String COL_BDATE = "date_begin";
	public static final String COL_EDATE = "date_end";
	
	
	/*****   Constructors   *****/
	
	/**
	 * Init SQLiteDAO with table "injury"
	 * 
	 * @param ctx In the example they passed "this" from the calling class..
	 *            I'm not really sure what this is yet.
	 */
	public InjuryModel(Context ctx)
	{
		super("injury", ctx);
	}
	
	/*****   Private   *****/
	
	/**
	 * Utility method to grab all the rows from a cursor
	 * 
	 * @param cr result of a query
	 * @return Array of entries
	 */
	private InjuryRow[] fetchInjuryRows(Cursor cr)
	{
		InjuryRow[] result = new InjuryRow[cr.getCount()];
		if (result.length == 0) {
			return result;
		}
		
		boolean valid = cr.moveToFirst();
		int ii = 0;
		
		// Grab the cursor's column indices
		// An error here indicates the COL constants aren't synced with the DB
		int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
		int ind_desc = cr.getColumnIndexOrThrow(COL_DESC);
		int ind_bd   = cr.getColumnIndexOrThrow(COL_BDATE);
		int ind_ed   = cr.getColumnIndexOrThrow(COL_EDATE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new InjuryRow();
			result[ii]._id         = cr.getLong(ind_id);
			result[ii].description = cr.getString(ind_desc);
			result[ii].date_begin  = cr.getInt(ind_bd);
			result[ii].date_end    = cr.getInt(ind_ed);
		
			valid = cr.moveToNext();
			ii ++;
		}
		
		return result;
	}
	
	/*****   Public   *****/
	
	/**
	 * Inserts a new entry into the workout table
	 * 
	 * @param row Add this entry to the DB
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(InjuryRow row)
	{
		return super.insert(row.toContentValues());
	}
	
	/**
	 * Inserts a new entry into the workout table, defaults record to 0
	 * 
	 * @param description
	 * @param date_begin Date injury started
	 * @param date_end Date injury ended
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String description, int date_begin, int date_end)
	{
		ContentValues cv = new ContentValues();
		cv.put(COL_DESC, description);
		cv.put(COL_BDATE, date_begin);
		cv.put(COL_EDATE, date_end);
		return super.insert(cv);
	}
	
	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public InjuryRow getByID(long id)
	{
		Cursor cr = selectByID(id);
		
		if (cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}
		
		InjuryRow[] rows = fetchInjuryRows(cr);
		return (rows.length == 0) ? null : rows[1];
	}

}