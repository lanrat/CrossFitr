package com.vorsk.crossfitr.models;

import android.content.ContentValues;

	
/**
 * AchievementModel entry struct
 * 
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class AchievementRow extends SQLiteRow
{
	// Cols
	public String name;
	public String description;
	public long achievement_type_id;
	public int progress_thresh;
	public int progress;
	public int count;
	
	public AchievementRow() {}
	
	public AchievementRow(ContentValues vals)
	{
		super(vals);
		name                = vals.getAsString(AchievementModel.COL_NAME);
		description         = vals.getAsString(AchievementModel.COL_DESC);
		achievement_type_id = vals.getAsLong(AchievementModel.COL_ACH_TYPE);
		progress_thresh     = vals.getAsInteger(AchievementModel.COL_THRESH);
		progress            = vals.getAsInteger(AchievementModel.COL_PROG);
		count               = vals.getAsInteger(AchievementModel.COL_COUNT);
	}

	public ContentValues toContentValues()
	{
		ContentValues vals = super.toContentValues();
		vals.put(AchievementModel.COL_NAME,     name);
		vals.put(AchievementModel.COL_DESC,     description);
		vals.put(AchievementModel.COL_ACH_TYPE, achievement_type_id);
		vals.put(AchievementModel.COL_THRESH,   progress_thresh);
		vals.put(AchievementModel.COL_PROG,     progress);
		vals.put(AchievementModel.COL_COUNT,    count);
		return vals;
	}
}