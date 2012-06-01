package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "profile" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of ProfileRow where each column
 * is a publicly accessible property.
 * 
 * @author Vivek
 * @since 1.0
 */
public class ProfileModel extends SQLiteDAO
{
	//// Constants
	
	// Table-specific columns
	public static final String COL_ATTR  = "attribute";
	public static final String COL_VALUE = "value";
	
	
	/*****   Constructors   *****/
	
	/**
	 * Init SQLiteDAO with table "profile"
	 * 
	 * @param ctx In the example they passed "this" from the calling class..
	 *            I'm not really sure what this is yet.
	 */
	public ProfileModel(Context ctx)
	{
		super("profile", ctx);
	}
	
	/*****   Private   *****/
	
	/**
	 * Utility method to grab all the rows from a cursor
	 * 
	 * @param cr result of a query
	 * @return Array of entries
	 */
	private ProfileRow[] fetchProfileRows(Cursor cr)
	{
		ProfileRow[] result = new ProfileRow[cr.getCount()];
		if (result.length == 0) {
			return result;
		}
		
		boolean valid = cr.moveToFirst();
		int ii = 0;
		
		// Grab the cursor's column indices
		// An error here indicates the COL constants aren't synced with the DB
		int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
		int ind_attr = cr.getColumnIndexOrThrow(COL_ATTR);
		int ind_val  = cr.getColumnIndexOrThrow(COL_VALUE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new ProfileRow();
			result[ii]._id       = cr.getLong(ind_id);
			result[ii].attribute = cr.getString(ind_attr);
			result[ii].value     = cr.getString(ind_val);
		
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
	public long insert(ProfileRow row)
	{
		return super.insert(row.toContentValues());
	}
	
	/**
	 * Inserts a new entry into the profile table, defaults record to 0
	 * 
	 * @param attr Attribute name
	 * @param value Value of the attribute
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String attr, String value)
	{
		ContentValues cv = new ContentValues();
		cv.put(COL_ATTR, attr);
		cv.put(COL_VALUE, value);
		return super.insert(cv);
	}
	
	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public ProfileRow getByID(long id)
	{
		Cursor cr = selectByID(id);
		
		// ID should never return more than 1 row...
		if (cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}
		
		ProfileRow[] rows = fetchProfileRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Fetch a specific profile attribute
	 *
	 * @param name Attribute name to retrieve
	 * @return Associated entry or NULL on failure
	 */
	public ProfileRow getByAttribute(String attr)
	{
		Cursor cr = select(
			new String[] { COL_ATTR },
			new String[] { attr }
		);
		
		// Name should be unique
		/*if (cr.getCount() > 1) {
			// TODO: Throw exception?
		}*/
		
		ProfileRow[] rows = fetchProfileRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Fetch all profile information
	 *
	 * @return List of each profile attribute and value
	 */
	public ProfileRow[] getAll()
	{
		Cursor cr = select(new String[] {}, new String[] {});
		
		return fetchProfileRows(cr);
	}

}