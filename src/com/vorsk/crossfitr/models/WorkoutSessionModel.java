package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "workout_session" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of WorkoutSessionRow where each column
 * is a publicly accessible property.
 * 
 * @author Vivek
 * @since 1.0
 */
public class WorkoutSessionModel extends SQLiteDAO
{
	//// Constants
	
	// Table-specific columns
	public static final String COL_WORKOUT    = "workout_id";
	public static final String COL_SCORE      = "score";
	public static final String COL_SCORE_TYPE = "score_type_id";
	
	
	/*****   Constructors   *****/
	
	/**
	 * Init SQLiteDAO with table "workout_session"
	 * 
	 * @param ctx In the example they passed "this" from the calling class..
	 *            I'm not really sure what this is yet.
	 */
	public WorkoutSessionModel(Context ctx)
	{
		super("workout_session", ctx);
	}
	
	/*****   Private   *****/
	
	/**
	 * Utility method to grab all the rows from a cursor
	 * 
	 * @param cr result of a query
	 * @return Array of entries
	 */
	private WorkoutSessionRow[] fetchWorkoutSessionRows(Cursor cr)
	{
		WorkoutSessionRow[] result = new WorkoutSessionRow[cr.getCount()];
		if (result.length == 0) {
			return result;
		}
		
		boolean valid = cr.moveToFirst();
		int ii = 0;
		
		// Grab the cursor's column indices
		// An error here indicates the COL constants aren't synced with the DB
		int ind_id    = cr.getColumnIndexOrThrow(COL_ID);
		int ind_wid   = cr.getColumnIndexOrThrow(COL_WORKOUT);
		int ind_score = cr.getColumnIndexOrThrow(COL_SCORE);
		int ind_stid  = cr.getColumnIndexOrThrow(COL_SCORE_TYPE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new WorkoutSessionRow();
			result[ii]._id           = cr.getLong(ind_id);
			result[ii].workout_id    = cr.getLong(ind_wid);
			result[ii].score         = cr.getInt(ind_score);
			result[ii].score_type_id = cr.getLong(ind_stid);
		
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
	public long insert(WorkoutSessionRow row)
	{
		return super.insert(row.toContentValues());
	}
	
	/**
	 * Inserts a new entry into the workout table, defaults record to 0
	 * 
	 * @param workout ID of the workout performed this session
	 * @param score The entry's score (time, reps, etc) or this.NOT_SCORED
	 * @param score_type Type of the score (this.SCORE_TIME, etc) or
	 *                   this.SCORE_NONE if no score is recorded
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(long workout, long score, long score_type)
	{
		Long isc = (score == NOT_SCORED) ? null : score;
		Long ist = (score_type == SCORE_NONE) ? null : score_type;
		
		ContentValues cv = new ContentValues();
		cv.put(COL_WORKOUT, workout);
		cv.put(COL_SCORE, isc);
		cv.put(COL_SCORE_TYPE, ist);
		return super.insert(cv);
	}
	
	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public WorkoutSessionRow getByID(long id)
	{
		Cursor cr = selectByID(id);
		
		if (cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}
		
		WorkoutSessionRow[] rows = fetchWorkoutSessionRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Remove a previously created session
	 * 
	 * Currently used by ResultsActivity is you don't want to save. This
	 * should be cleaned up so this method can be removed.
	 * 
	 * @param id session_id to delete
	 * @return result of deletion, -1 on failure
	 */
	public int delete(long id)
	{
		return super.delete(COL_ID + " = " + id);
	}

}