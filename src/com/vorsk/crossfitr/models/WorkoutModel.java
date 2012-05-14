package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "workout" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of WorkoutModel.Row where each column is a
 * publicly accessible property.
 * 
 * @author Vivek
 * @since 1.0
 */
public class WorkoutModel extends SQLiteDAO
{
	//// Constants
	
	// Table-specific columns
	public static final String COL_WK_TYPE  = "workout_type_id";
	public static final String COL_RECORD   = "record";
	public static final String COL_REC_TYPE = "record_type_id";
	
	/**
	 * Workout entry struct
	 * 
	 * This is a customized data container to hold an entry from the workout
	 * table. Every DAO Model will have its own Row class definition.
	 */
	public class Row extends SQLiteDAO.Row
	{
		// Cols
		public String name;
		public String description;
		public long   workout_type_id;
		public int    record;
		public long   record_type_id;
		
		public Row() {}
		
		public Row(ContentValues vals)
		{
			super(vals);
			name            = vals.getAsString(COL_NAME);
			description     = vals.getAsString(COL_DESC);
			workout_type_id = vals.getAsLong(COL_WK_TYPE);
			record = vals.getAsInteger(COL_RECORD);
			record_type_id = vals.getAsLong(COL_REC_TYPE);
		}

		public ContentValues toContentValues()
		{
			ContentValues vals = super.toContentValues();
			vals.put(COL_NAME,  name);
			vals.put(COL_DESC,  description);
			vals.put(COL_WK_TYPE,  workout_type_id);
			vals.put(COL_RECORD,   record);
			vals.put(COL_REC_TYPE, record_type_id);
			return vals;
		}
	}
	
	
	/*****   Constructors   *****/
	
	/**
	 * Init SQLiteDAO with table "workout"
	 * 
	 * @param ctx In the example they passed "this" from the calling class..
	 *            I'm not really sure what this is yet.
	 */
	public WorkoutModel(Context ctx)
	{
		super("workout", ctx);
	}
	
	/*****   Private   *****/
	
	/**
	 * Utility method to grab all the rows from a cursor
	 * 
	 * @param cr result of a query
	 * @return Array of entries
	 */
	private Row[] fetchRows(Cursor cr)
	{
		Row[] result = new Row[cr.getCount()];
		if (result.length == 0) {
			return result;
		}
		
		boolean valid = cr.moveToFirst();
		int ii = 0;
		
		// Grab the cursor's column indices
		// An error here indicates the COL constants aren't synced with the DB
		int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
		int ind_name = cr.getColumnIndexOrThrow(COL_NAME);
		int ind_desc = cr.getColumnIndexOrThrow(COL_DESC);
		int ind_wtid = cr.getColumnIndexOrThrow(COL_WK_TYPE);
		int ind_rec = cr.getColumnIndexOrThrow(COL_RECORD);
		int ind_rtid = cr.getColumnIndexOrThrow(COL_REC_TYPE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new Row();
			result[ii]._id = cr.getLong(ind_id);
			result[ii].name = cr.getString(ind_name);
			result[ii].description = cr.getString(ind_desc);
			result[ii].workout_type_id = cr.getLong(ind_wtid);
			result[ii].record = cr.getInt(ind_rec);
			result[ii].record_type_id = cr.getLong(ind_rtid);

			valid = cr.moveToNext();
			ii++;
		}

		return result;
	}
	
	/*****   Public   *****/
	
	/**
	 * Inserts a new entry into the workout table
	 * 
	 * @param row
	 *            Add this entry to the DB
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(Row row) {
		return super.insert(row.toContentValues());
	}

	/**
	 * Inserts a new entry into the workout table, defaults record to 0
	 * 
	 * @param name
	 * @param desc
	 * @param type Type of the workout (this.TYPE_GIRL, etc)
	 * @param rec_type Type of scoring used (this.SCORE_TIME, etc)
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String name, String desc, int type, int rec_type) {
		// Default COL_RECORD to 0
		return insert(name, desc, type, rec_type, 0);
	}

	/**
	 * Inserts a new entry into the workout table
	 * 
	 * @param name
	 * @param desc
	 * @param type Type of the workout (this.TYPE_GIRL, etc)
	 * @param rec_type Type of scoring used (this.SCORE_TIME, etc)
	 * @param record Best score received on this workout or this.NOT_SCORED
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String name, String desc, int type, int rec_type,
	                   int record)
	{
		String srtype = (rec_type == SCORE_NONE) 
				? null : String.valueOf(rec_type);
		String srec = (record == NOT_SCORED) ? null : String.valueOf(record);
		
		ContentValues cv = new ContentValues();
		cv.put(name, COL_NAME);
		cv.put(desc, COL_DESC);
		cv.put(String.valueOf(srtype), COL_WK_TYPE);
		cv.put(srtype, COL_REC_TYPE);
		cv.put(srec, COL_RECORD);
		return super.insert(cv);
	}

	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public Row getByID(long id) {
		Cursor cr = selectByID(id);

		if (cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}

		Row[] rows = fetchRows(cr);
		return (rows.length == 0) ? null : rows[1];
	}

	/**
	 * Fetch all workouts of a specific type (girl, hero, custom, wod)
	 * 
	 * @param type
	 *            The workout type; use constants (TYPE_GIRL, etc)
	 * @return
	 */
	public Row[] getAllByType(int type) {
		String[] col = new String[] { COL_WK_TYPE };
		String[] val = new String[] { String.valueOf(type) };
		Cursor cr = select(col, val);
		return fetchRows(cr);
	}

}