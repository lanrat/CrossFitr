package com.vorsk.crossfitr.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class WorkoutModel extends SQLiteDAO {
	// // Constants
	public static final String COL_WK_TYPE = "workout_type_id";
	public static final String COL_RECORD = "record";
	public static final String COL_REC_TYPE = "record_type_id";

	// // Context struct
	public class Row {
		// Cols
		public long _id;
		public String name;
		public String description;
		public long workout_type_id;
		public int record;
		public long record_type_id;

		public Row() {
			// TODO Auto-generated constructor stub
		}

		public Row(ContentValues vals) {
			_id = vals.getAsLong(COL_ID);
			name = vals.getAsString(COL_NAME);
			description = vals.getAsString(COL_DESC);
			workout_type_id = vals.getAsLong(COL_WK_TYPE);
			record = vals.getAsInteger(COL_RECORD);
			record_type_id = vals.getAsLong(COL_REC_TYPE);
		}

		public ContentValues toContentValues() {
			ContentValues vals = new ContentValues();
			vals.put(COL_ID, _id);
			vals.put(COL_NAME, name);
			vals.put(COL_DESC, description);
			vals.put(COL_WK_TYPE, workout_type_id);
			vals.put(COL_RECORD, record);
			vals.put(COL_REC_TYPE, record_type_id);
			return vals;
		}
	}

	/*** Constructors ***/

	public WorkoutModel(Context ctx) {
		super("workout", ctx);
	}

	/*** Private ***/

	private Row[] fetchRows(Cursor cr) {
		Row[] result = new Row[cr.getCount()];
		if (result.length == 0)
			return result;

		boolean valid = cr.moveToFirst();
		int ii = 0;

		int ind_id = cr.getColumnIndexOrThrow(COL_ID);
		int ind_name = cr.getColumnIndexOrThrow(COL_NAME);
		int ind_desc = cr.getColumnIndexOrThrow(COL_DESC);
		int ind_wtid = cr.getColumnIndexOrThrow(COL_WK_TYPE);
		int ind_rec = cr.getColumnIndexOrThrow(COL_RECORD);
		int ind_rtid = cr.getColumnIndexOrThrow(COL_REC_TYPE);

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

	/*** Public ***/

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
	 * @param type
	 * @param rec_type
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
	 * @param type
	 * @param rec_type
	 * @param record
	 * @return ID of newly added entry, -1 on failure
	 */
	public long insert(String name, String desc, int type, int rec_type,
			int record) {
		ContentValues cv = new ContentValues();
		cv.put(name, COL_NAME);
		cv.put(desc, COL_DESC);
		cv.put(String.valueOf(type), COL_WK_TYPE);
		cv.put(String.valueOf(rec_type), COL_REC_TYPE);
		cv.put(String.valueOf(record), COL_RECORD);
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