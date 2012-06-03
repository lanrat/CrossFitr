package com.vorsk.crossfitr.models;

import android.content.ContentValues;


/**
 * Workout Session entry struct
 * 
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class WorkoutSessionRow extends SQLiteRow
{
	// Cols
	public long   workout_id;
	public int    score;
	public long   score_type_id;
	public String comments;
	
	public WorkoutSessionRow() {}
	
	public WorkoutSessionRow(ContentValues vals)
	{
		super(vals);
		workout_id     = vals.getAsLong(WorkoutSessionModel.COL_WORKOUT);
		score          = vals.getAsInteger(WorkoutSessionModel.COL_SCORE);
		score_type_id  = vals.getAsLong(WorkoutSessionModel.COL_SCORE_TYPE);
		comments       = vals.getAsString(WorkoutSessionModel.COL_CMNT);
	}

	public ContentValues toContentValues()
	{
		ContentValues vals = super.toContentValues();
		vals.put(WorkoutSessionModel.COL_WORKOUT,    workout_id);
		vals.put(WorkoutSessionModel.COL_SCORE,      score);
		vals.put(WorkoutSessionModel.COL_SCORE_TYPE, score_type_id);
		vals.put(WorkoutSessionModel.COL_CMNT, comments);
		return vals;
	}
}