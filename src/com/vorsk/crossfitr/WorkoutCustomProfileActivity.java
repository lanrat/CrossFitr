package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;
import com.vorsk.crossfitr.models.WorkoutSessionModel;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WorkoutCustomProfileActivity extends Activity implements OnClickListener 
{
	private WorkoutRow workout;
	private int ACT_TIMER = 1;
	private Typeface font;
	TextView screenName, tvname, tvdesc, tvbestRecord;
	
	WorkoutModel model = new WorkoutModel(this);
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//get the id passed from previous activity (workout lists)
		long id = getIntent().getLongExtra("ID", -1);
		//if ID is invalid, go back to home screen
		if(id < 0)
		{
			finish();
		}
		//set view
		setContentView(R.layout.workout_custom_profile);
		
		//create a WorkoutRow, to retrieve data from database
		model.open();
		workout = model.getByID(id);
		
		//TextView objects

		font = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Thin.ttf");
		screenName = (TextView) findViewById(R.id.screenTitle);
		screenName.setTypeface(font);
		tvname = (TextView) findViewById(R.id.workout_profile_nameDB);
		tvname.setTypeface(font);
		tvbestRecord = (TextView) findViewById(R.id.workout_profile_best_recordDB);
		tvbestRecord.setTypeface(font);
		tvdesc = (TextView) findViewById(R.id.workout_profile_descDB);
		tvdesc.setTypeface(font);
		
		//set the text of the TextView objects from the data retrieved from the DB
		Resources res = getResources();
		tvname.setText(workout.name);
		if (model.getTypeName(workout.workout_type_id).equals("WOD"))
			tvname.setTextColor(res.getColor(R.color.wod));
		else if (model.getTypeName(workout.workout_type_id).equals("Hero"))
			tvname.setTextColor(res.getColor(R.color.heroes));
		else if (model.getTypeName(workout.workout_type_id).equals("Girl"))
			tvname.setTextColor(res.getColor(R.color.girls));
		else if(model.getTypeName(workout.workout_type_id).equals("Custom"))
			tvname.setTextColor(res.getColor(R.color.custom));
		tvdesc.setText(workout.description);
		//tvrecordType.setText(model.getTypeName(workout.workout_type_id));
        model.close();
        
		// begin workout button
        View beginButton = findViewById(R.id.button_begin_workout);
        ((TextView) beginButton).setTypeface(font);
        beginButton.setOnClickListener(this);
        
        View editButton = findViewById(R.id.button_custom_edit_button);
        ((TextView) editButton).setTypeface(font);
        editButton.setOnClickListener(this);
        
        View deleteButton = findViewById(R.id.button_custom_delete_button);
        ((TextView) deleteButton).setTypeface(font);
        deleteButton.setOnClickListener(this);
        
        if (workout.description.indexOf("Rest Day") == -1){
        	//It is not a rest day
    		tvbestRecord.setText("personal record: "+StopwatchActivity.formatElapsedTime(Long.parseLong(String.valueOf(workout.record))));
        	beginButton.setOnClickListener(this);
        }else{
        	//it is a rest day
        	beginButton.setVisibility(View.GONE);
        	tvbestRecord.setVisibility(View.GONE);
        }
	}
	
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		    // if user presses begin button, user will now go into the timer page.
			case R.id.button_begin_workout:
				Intent i = new Intent(this, TimeTabWidget.class);
				i.putExtra("workout_id", workout._id);
				startActivityForResult(i, ACT_TIMER);
				break;
				
			case R.id.button_custom_edit_button:
				Intent u = new Intent(this, CustomEditActivity.class);
				u.putExtra("id", workout._id);
				startActivity(u);
				break;
			case R.id.button_custom_delete_button:
				model.open();
				model.delete(workout._id);
				model.close();
					
				Intent j = new Intent(this, CustomActivity.class);
				startActivity(j);
				
				break;
		}
	}
	
	protected void onActivityResult(int request, int result, Intent data)
	{
		if (request == ACT_TIMER) {
			if (result != RESULT_CANCELED) {
				// Session was completed
				long score;
				WorkoutSessionModel model = new WorkoutSessionModel(this);
				model.open();
				
				// Get the score returned
				if (workout.record_type_id == WorkoutModel.SCORE_TIME) {
					score = data.getLongExtra("time", -1);
				} else if (workout.record_type_id == WorkoutModel.SCORE_REPS) {
					score = WorkoutModel.NOT_SCORED; // TODO: this
				} else if (workout.record_type_id == WorkoutModel.SCORE_WEIGHT) {
					score = WorkoutModel.NOT_SCORED; // TODO: this
				} else {
					score = WorkoutModel.NOT_SCORED;
				}
				
				// Save as a new session
				long id = model.insert(workout._id, score,
						workout.record_type_id);
				model.close();
				
				// Show the results page
				Intent res = new Intent(this, ResultsActivity.class);
				res.putExtra("session_id", id);
				startActivity(res);
			}
		}
	}
}
