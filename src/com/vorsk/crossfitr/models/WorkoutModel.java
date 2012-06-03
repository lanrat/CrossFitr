package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * DAO for "workout" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of WorkoutRow where each column is a
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
	
	private static String TAG = "WorkoutModel";
	
	
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
	private WorkoutRow[] fetchWorkoutRows(Cursor cr)
	{
		if (cr == null) {
			return null;
		}
		WorkoutRow[] result = new WorkoutRow[cr.getCount()];
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
			result[ii] = new WorkoutRow();
			result[ii]._id = cr.getLong(ind_id);
			result[ii].name = cr.getString(ind_name);
			result[ii].description = cr.getString(ind_desc);
			result[ii].workout_type_id = cr.getLong(ind_wtid);
			result[ii].record = cr.getInt(ind_rec);
			result[ii].record_type_id = cr.getLong(ind_rtid);

			valid = cr.moveToNext();
			ii++;
		}
		
		cr.close();
		return result;
	}
	
	/*****   Public   *****/
	
	/**
	 * Inserts a new entry into the workout table
	 * TODO: does not work, violates unknown constraint
	 * @param row
	 *            Add this entry to the DB
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(WorkoutRow row) {
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
		// Default COL_RECORD to NOT_SCORED
		return insert(name, desc, type, rec_type, NOT_SCORED);
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
		Integer wtype = (type == TYPE_NONE) ? null : type;
		Integer rtype = (rec_type == SCORE_NONE) ? null : rec_type;
		Integer rec = (record == NOT_SCORED) ? null : record;
		
		ContentValues cv = new ContentValues();
		cv.put(COL_NAME, name);
		cv.put(COL_DESC, desc);
		cv.put(COL_WK_TYPE, wtype);
		cv.put(COL_REC_TYPE, rtype);
		cv.put(COL_RECORD, rec);
		return super.insert(cv);
	}

	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public WorkoutRow getByID(long id)
	{
		Cursor cr = selectByID(id);

		if (cr == null || cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}

		WorkoutRow[] rows = fetchWorkoutRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Finds a workout matching a given name
	 * 
	 * @param name the name to search for
	 * @return the row id containing that workout, -1 on failure;
	 */
	public long getIDFromName(String name){
		/*Log.d(TAG,"looking up id for: "+name);
		Log.d(TAG,"Table: "+DB_TABLE);
		Cursor cr = db.rawQuery(
				"SELECT "+COL_ID+" FROM " + DB_TABLE + " WHERE " + COL_NAME + " = ?",new String[] {name});
		if (cr != null && cr.getCount() > 0){
			Log.d(TAG,"found id");
			int id = cr.getColumnIndex(COL_ID);
			Log.d(TAG,"id is: "+id);
			return cr.getLong(id);
			//return fetchWorkoutRows(cr)[0]._id;
		}
		Log.d(TAG,"name not found");
		return -1;	*/
		return selectIDByName(DB_TABLE, name);
	}

	/**
	 * Fetch all workouts of a specific type (girl, hero, custom, wod)
	 * 
	 * @param type The workout type; use constants (TYPE_GIRL, etc)
	 * @return array of workouts, null on failure
	 */
	public WorkoutRow[] getAllByType(int type)
	{
		String[] col = { COL_WK_TYPE };
		String[] val = { String.valueOf(type) };
		Cursor cr = select(col, val);
		return fetchWorkoutRows(cr);
	}
	
	/**
	 * Gets a workout_type's ID by its name
	 * 
	 * @param name
	 * @return ID of the workout type, -1 on failure
	 */
	public long getTypeID(String name)
	{
		return selectIDByName("workout_type", name);
	}
	
	/**
	 * Gets a workout_type's name by its ID
	 * 
	 * @param id
	 * @return name of the workout type, NULL on failure
	 */
	public String getTypeName(long id)
	{
		return selectNameByID("workout_type", id);
	}

}
