package com.vorsk.crossfitr.models;

import android.content.ContentValues;


/**
 * ProfileModel entry struct
 * 
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class ProfileRow extends SQLiteRow
{
	// Cols
	public String attribute;
	public String value;
	
	public ProfileRow() {}
	
	public ProfileRow(ContentValues vals)
	{
		super(vals);
		attribute = vals.getAsString(ProfileModel.COL_ATTR);
		value = vals.getAsString(ProfileModel.COL_VALUE);
	}

	public ContentValues toContentValues()
	{
		ContentValues vals = super.toContentValues();
		vals.put(ProfileModel.COL_ATTR,  attribute);
		vals.put(ProfileModel.COL_VALUE, value);
		return vals;
	}
}