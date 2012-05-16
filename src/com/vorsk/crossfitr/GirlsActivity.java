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
	/*
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Cursor mCursor = getContentResolver().query(WorkoutModel.Row.workout_type_id);
		startManagingCursor(mCursor);
		ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, mCursor, 
												      WorkoutModel.Row.name);
		setListAdapter(adapter);
	}
	*/
	// Static List Implementation
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String[] GIRLS = new String[] { "GirlA", "GirlB", "GirlC", "GirlD", "GirlE" };
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, GIRLS);
		
		setListAdapter(adapter);
	}
	
	
}