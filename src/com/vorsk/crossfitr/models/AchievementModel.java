package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "Achievement" table.
 * 
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of AchievementRow where each column
 * is a publicly accessible property.
 * 
 * @author Vivek and Sam
 * @since 1.0
 */
public class AchievementModel extends SQLiteDAO
{
	//// Constants
	
	// Table-specific columns
	public static final String COL_NAME     = "name";
	public static final String COL_DESC     = "description";
	public static final String COL_ACH_TYPE = "achievement_type_id";
	public static final String COL_THRESH   = "progress_thresh";
	public static final String COL_PROG     = "progress";
	public static final String COL_COUNT    = "count";
	
	
	/*****   Constructors   *****/
	
	/**
	 * Init SQLiteDAO with table "Achievement"
	 * 
	 * @param ctx In the example they passed "this" from the calling class..
	 *            I'm not really sure what this is yet.
	 */
	public AchievementModel(Context ctx)
	{
		super("achievement", ctx);
	}
	
	/*****   Private   *****/
	
	/**
	 * Utility method to grab all the rows from a cursor
	 * 
	 * @param cr result of a query
	 * @return Array of entries
	 */
	private AchievementRow[] fetchAchievementRows(Cursor cr)
	{
		if (cr == null) {
			return null;
		}
		AchievementRow[] result = new AchievementRow[cr.getCount()];
		if (result.length == 0) {
			cr.close();
			return result;
		}
		
		boolean valid = cr.moveToFirst();
		int ii = 0;
		
		// Grab the cursor's column indices
		// An error here indicates the COL constants aren't synced with the DB
		int ind_id    = cr.getColumnIndexOrThrow(COL_ID);
		int ind_name  = cr.getColumnIndexOrThrow(COL_NAME);
		int ind_desc  = cr.getColumnIndexOrThrow(COL_DESC);
		int ind_atid  = cr.getColumnIndexOrThrow(COL_ACH_TYPE);
		int ind_thres = cr.getColumnIndexOrThrow(COL_THRESH);
		int ind_prog  = cr.getColumnIndexOrThrow(COL_PROG);
		int ind_cnt   = cr.getColumnIndexOrThrow(COL_COUNT);
		int ind_dm    = cr.getColumnIndexOrThrow(COL_MDATE);
		int ind_dc    = cr.getColumnIndexOrThrow(COL_CDATE);
		
		// Iterate over every row (move the cursor down the set)
		while (valid) {
			result[ii] = new AchievementRow();
			fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
			result[ii].name                = cr.getString(ind_name);
			result[ii].description         = cr.getString(ind_desc);
			result[ii].achievement_type_id = cr.getLong(ind_atid);
			result[ii].progress_thresh     = cr.getInt(ind_thres);
			result[ii].progress            = cr.getInt(ind_prog);
			result[ii].count               = cr.getInt(ind_cnt);
		
			valid = cr.moveToNext();
			ii ++;
		}

		cr.close();
		return result;
	}
	
	/*****   Public   *****/
	
	/**
	 * Inserts a new entry into the workout table
	 * 
	 * @param row Add this entry to the DB
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(AchievementRow row)
	{
		return super.insert(row.toContentValues());
	}
	
	/**
	 * Inserts a new entry into the achievement table, defaults record to 0
	 * 
	 * @param name
	 * @param description
	 * @param achievement_type Type of the achievement.. TODO
	 * @param threshold Limit of the progress before awarded
	 * @param progress Progress towards getting this
	 * @param count number of times this was earned
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String name, String description, long achievement_type,
			int threshold, int progress, int count)
	{
		ContentValues cv = new ContentValues();
		cv.put(COL_NAME, name);
		cv.put(COL_DESC, description);
		cv.put(COL_ACH_TYPE, achievement_type);
		cv.put(COL_THRESH, threshold);
		cv.put(COL_PROG, progress);
		cv.put(COL_COUNT, count);
		return super.insert(cv);
	}
	
	/**
	 * Attempts to update a previously entered achievement. If the update fails,
	 * or the achievement has not yet passed the threshold it returns null. If it 
	 * has passed the threshold it will return the description for a display toast.
	 * 
	 * @param achievementType
	 *            Type of achievement to update
	 * @return AchievementRow containing achievement if threshold
	 * 		   is passed, null if not, or failed.
	 */
	public String getProgress(int achievementType) {
		AchievementRow[] achievement;
		
		// Check if the all workout achievements need to be updated.
		if(achievementType == AchievementModel.TYPE_GIRL || achievementType == AchievementModel.TYPE_HERO){
			AchievementRow[] all = this.getAllByType(AchievementModel.TYPE_ALL);
			AchievementRow[] other = this.getAllByType(achievementType);
			achievement = new AchievementRow[all.length + other.length];
			System.arraycopy(all, 0, achievement, 0, all.length);
			System.arraycopy(other, 0, achievement, all.length, other.length);
		}
		else{
			achievement = this.getAllByType(achievementType);
		}
		
		ContentValues cv;
		String totalAchievement = null;
		
		for(int i = 0; i < achievement.length; i++)
		{
			cv  = new ContentValues();
			cv.put(COL_NAME, achievement[i].name);
		
			// Increment progress of achievement
			int newProgress = achievement[i].progress;
			newProgress += 1;
			cv.put(COL_PROG, newProgress);
			
			// If the progress has passed the threshold, update the count
			// and return the name of the achievement
			if(newProgress >= (achievement[i].progress_thresh) &&
			   achievement[i].count == 0){
				cv.put(COL_COUNT, 1);
				super.update(cv, "name='" + achievement[i].name + "'");
				totalAchievement += achievement[i].name + "\n";
			}
			else{
				super.update(cv, "name='" + achievement[i].name + "'");
			}
		}
		return totalAchievement;
	}
	
	/**
	 * Fetch all achievements of a specific type (girl, hero, custom, all)
	 * 
	 * @param type The achievement type; use constants (TYPE_GIRL, etc)
	 * @return array of achievements, null on failure
	 */
	public AchievementRow[] getAllByType(int type)
	{
		String[] col = { COL_ACH_TYPE };
		String[] val = { String.valueOf(type) };
		Cursor cr = select(col, val);
		return fetchAchievementRows(cr);
	}
	/**
	 * Fetch a specific achievement by name
	 * 
	 * @param name
	 *            Achievement name to retrieve
	 * @return Associated entry or NULL on failure
	 */
	public AchievementRow getByName(String name) {
		Cursor cr = select(new String[] { COL_NAME }, new String[] { name});

		// Name should be unique
		/*
		 * if (cr.getCount() > 1) { // TODO: Throw exception? }
		 */

		AchievementRow[] rows = fetchAchievementRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Fetch an entry via the ID
	 * 
	 * @param id
	 * @return Associated entry or NULL on failure
	 */
	public AchievementRow getByID(long id)
	{
		Cursor cr = selectByID(id);
		
		if (cr.getCount() > 1) {
			return null; // TODO: Throw exception
		}
		
		AchievementRow[] rows = fetchAchievementRows(cr);
		return (rows.length == 0) ? null : rows[0];
	}
	
	/**
	 * Gets the total number of sessions performed
	 * 
	 * @return Total sessions
	 */
	public int getTotal()
	{
		return selectCount(null, null);
	}

}