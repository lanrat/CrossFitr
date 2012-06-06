package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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
	
	private Context context;
		
	
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
		context = ctx;
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
			cr.close();
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
		int ind_rec  = cr.getColumnIndexOrThrow(COL_RECORD);
		int ind_rtid = cr.getColumnIndexOrThrow(COL_REC_TYPE);
		int ind_dm   = cr.getColumnIndexOrThrow(COL_MDATE);
		int ind_dc   = cr.getColumnIndexOrThrow(COL_CDATE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new WorkoutRow();
			fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
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
	public long insert(WorkoutRow row)
	{
		// TODO: Fix this? Seemed to be funky
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
	 * Call this to edit the properties of a workout entry
	 * 
	 * @param row The new data to replace
	 * @return Number of rows affected
	 */
	public long edit(WorkoutRow row)
	{
		return super.update(row.toContentValues(), COL_ID + " = " + row._id);
	}
	
	/**
	 * Remove a workout definition. History for it will be removed
	 * 
	 * @param id Workout definition ID to remove
	 * @return Number of removed workouts
	 */
	public long delete(long id)
	{
		WorkoutSessionModel model = new WorkoutSessionModel(context);
		model.deleteWorkoutHistory(id);
		return super.delete(COL_ID + " = " + id);
	}
	
	/**
	 * Recalculates the best record from existing sessions
	 * 
	 * @param id Workout ID to recalculate
	 * @param type Type of the Workout ID
	 * @return Number of rows updated; -1 or 0 on failure
	 */
	public int calculateRecord(long id, long type)
	{
		String cond;
		if (type == SCORE_TIME) {
			cond = "MIN";
		} else if (type == SCORE_WEIGHT || type == SCORE_REPS) {
			cond = "MAX";
		} else {
			return -1;
		}
		
		WorkoutSessionModel model = new WorkoutSessionModel(context);
		ContentValues cv = new ContentValues();
		cv.put(COL_RECORD, model.getWorkoutAggScore(id, cond));
		
		return update(cv, COL_ID + "=" + id);
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
	public long getIDFromName(String name)
	{
		return super.selectIDByName(DB_TABLE, name);
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
	public  String getTypeName(long id)
	{
		return selectNameByID("workout_type", id);
	}

}
