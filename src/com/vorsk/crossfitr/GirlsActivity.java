package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.vorsk.crossfitr.models.WorkoutModel;

public class GirlsActivity extends ListActivity
{
	/* Database List Work in Progress
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Cursor mCursor = getGirlWorkouts();
		startManagingCursor(mCursor);
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, mCursor, 
												      new String[] { ContactsContract.Contacts._ID,
						                              ContactsContract.Contacts.DISPLAY_NAME },
						                              new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}
	
	private Cursor getGirlWorkouts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
				+ ("1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return managedQuery(uri, projection, selection, selectionArgs,
				sortOrder);
	}
	*/
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String[] GIRLS = new String[] { "GirlA", "GirlB", "GirlC", "GirlD", "GirlE" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, GIRLS);
		
		setListAdapter(adapter);
	}
	
	
}