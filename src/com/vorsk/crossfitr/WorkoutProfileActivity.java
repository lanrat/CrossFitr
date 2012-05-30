package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WorkoutProfileActivity extends Activity implements OnClickListener 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//create model object
		WorkoutModel model = new WorkoutModel(this);
		//open model to put data into database
		model.open();
		//get the id passed from previous activity (workout lists)
		long id = getIntent().getLongExtra("ID", -1);
		//if ID is invalid, go back to home screen
		if(id < 0)
		{
			startActivity(new Intent(this, CrossFitrActivity.class));
		}
		//set view
		setContentView(R.layout.workout_profile);
		
		Log.v("VIEW", "ID = " + id);
		//create a WorkoutRow, to retrieve data from database
		WorkoutRow row = model.getByID(id);
		
		//TextView objects
		TextView tvname = (TextView)findViewById(R.id.workout_profile_nameDB);
		TextView tvdesc = (TextView)findViewById(R.id.workout_profile_descDB);
		TextView tvrecordType = (TextView)findViewById(R.id.workout_profile_recordtypeDB); 
		TextView tvbestRecord = (TextView)findViewById(R.id.workout_profile_best_recordDB);
		
		//set the texts of the TextView objects from the data retrieved from the DB
		tvname.setText(row.name);
		tvdesc.setText(row.description);
		tvrecordType.setText(model.getTypeName(row.workout_type_id));//TODO: convert later after method
		tvbestRecord.setText(String.valueOf(row.record));
        
		// begin workout button
        View beginButton = findViewById(R.id.button_begin_workout);
        beginButton.setOnClickListener(this);
        model.close();
	}
	
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		    // if user presses begin button, user will now go into the timer page.
			case R.id.button_begin_workout:
				Intent i = new Intent(this, TimeTabWidget.class);
				startActivity(i);
				break;
		}
	}
}
