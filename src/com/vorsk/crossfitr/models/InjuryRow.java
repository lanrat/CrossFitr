package com.vorsk.crossfitr.models;

import android.content.ContentValues;


/**
 * injury entry struct
 * 
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class InjuryRow extends SQLiteRow
{
	// Cols
	public String description;
	public int date_begin;
	public int date_end;
	
	public InjuryRow() {}
	
	public InjuryRow(ContentValues vals)
	{
		super(vals);
		description = vals.getAsString(InjuryModel.COL_DESC);
		date_begin  = vals.getAsInteger(InjuryModel.COL_BDATE);
		date_end    = vals.getAsInteger(InjuryModel.COL_EDATE);
	}

	@Override
	public ContentValues toContentValues()
	{
		ContentValues vals = super.toContentValues();
		vals.put(InjuryModel.COL_DESC,  description);
		vals.put(InjuryModel.COL_BDATE, date_begin);
		vals.put(InjuryModel.COL_EDATE, date_end);
		return vals;
	}
}