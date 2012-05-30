package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HeroesActivity extends Activity implements OnItemClickListener 
{
	//private adapter
	private ArrayAdapter<WorkoutRow> adapter;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);
		
		WorkoutModel model = new WorkoutModel(this);

		//Access the database and retrieve all heroes workouts
		model.open();	
		WorkoutRow[] results = model.getAllByType(WorkoutModel.TYPE_HERO);
		model.close();
		
        ListView lv = (ListView) findViewById(R.id.workout_list_view);
        		
		adapter = new ArrayAdapter<WorkoutRow>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, results);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		//pass the ID of the workout into the WorkoutProfileActivity
		WorkoutRow workout = adapter.getItem(position);
		Intent x = new Intent(this, WorkoutProfileActivity.class);
		x.putExtra("ID", workout._id);
		startActivity(x);
	}
}