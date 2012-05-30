package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class CustomActivity extends Activity implements OnClickListener,
                                                        OnItemClickListener
{
	private ArrayAdapter<WorkoutRow> adapter;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_workout_list);
        
        ListView lv = (ListView) findViewById(R.id.workout_list_view);
        
		View add_custom_button = findViewById(R.id.add_custom_button);
		add_custom_button.setOnClickListener(this);
		
		View edit_custom_button = findViewById(R.id.edit_custom_button);
		add_custom_button.setOnClickListener(this);
		
		WorkoutModel model = new WorkoutModel(this);

		//Access the database and retrieve all custom workouts
		model.open();	
		WorkoutRow[] results = model.getAllByType(WorkoutModel.TYPE_CUSTOM);
		model.close();

		adapter = new ArrayAdapter<WorkoutRow>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, results);

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
			case R.id.add_custom_button:
				Intent u = new Intent(this, AddCustomActivity.class);
				startActivity(u);
				break;
			case R.id.edit_custom_button:
				Intent t = new Intent(this, AddCustomActivity.class);
				startActivity(t);
				break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		WorkoutRow workout = adapter.getItem(position);
		Intent x = new Intent(this, WorkoutProfileActivity.class);
		x.putExtra("ID", workout._id);
		startActivity(x);
	}
	
}