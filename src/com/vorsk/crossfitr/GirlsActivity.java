package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.*;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class GirlsActivity extends Activity implements OnItemClickListener
{
	//private adapter
	private ArrayAdapter<WorkoutRow> adapter;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		WorkoutModel model = new WorkoutModel(this);
		
		//Access the database and retrieve all girl workouts
		model.open();	
		WorkoutRow[] results = model.getAllByType(WorkoutModel.TYPE_GIRL);
		model.close();
		
        ListView lv = (ListView) findViewById(R.id.workout_list_view);
        		
		adapter = new ArrayAdapter<WorkoutRow>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, results);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//pass the ID of the workout into the WorkoutProfileActivity
		WorkoutRow workout = adapter.getItem(position);
		Intent x = new Intent(this, WorkoutProfileActivity.class);
		x.putExtra("ID", workout._id);
		startActivity(x);
	}
}