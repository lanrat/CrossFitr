package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;
import com.vorsk.crossfitr.models.WorkoutSessionModel;
import com.vorsk.crossfitr.models.WorkoutSessionRow;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener
{
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle params = getIntent().getExtras();
		
		long session_id = params.getLong("session_id");
		
		// Get the relevant session data
		WorkoutSessionModel wsmodel = new WorkoutSessionModel(this);
		wsmodel.open();
		WorkoutSessionRow session = wsmodel.getByID(session_id);
		if (session == null) {
			Log.v("Page_Requirement", "invalid session id was provided");
			finish();
		}
		wsmodel.close();
		
		WorkoutModel wmodel = new WorkoutModel(this);
		wmodel.open();
		WorkoutRow workout = wmodel.getByID(session.workout_id);
		if (workout == null) {
			Log.e("DB_Inconsistency", "Invalid workout id on session");
			finish();
		}
		wmodel.close();
		
		// Create the initial view objects
		setContentView(R.layout.results);
		
		// Get views
		View btn_save = findViewById(R.id.button_results_sav_workout);
		View btn_nosave = findViewById(R.id.button_results_dontsav_workout);
		View btn_sharefb = findViewById(R.id.button_results_share_workout_FB);
		
		TextView txt_name = (TextView)findViewById(R.id.text_workout_name);
		TextView txt_desc = (TextView)findViewById(R.id.text_workout_desc);
		TextView txt_record = (TextView)findViewById(R.id.text_workout_record);
		TextView txt_score = (TextView)findViewById(R.id.text_session_score);
		
		// Set content
		txt_name.setText(workout.name);
		txt_desc.setText(workout.description);
		txt_record.setText(workout.record);
		txt_score.setText(session.score);

		// Set handlers
		btn_save.setOnClickListener(this);
		btn_nosave.setOnClickListener(this);
		btn_sharefb.setOnClickListener(this);
	}
	
	public void onClick(View v)
	{
		switch(v.getId())
		{
			// if user presses save and end button button, will go back to home screen after saving.
			case R.id.button_results_sav_workout:
				//TODO: save data to db
				Intent i = new Intent(this, CrossFitrActivity.class);
				startActivity(i);
				break;
			// if user presses dont save button, go back to home screen.
			case R.id.button_results_dontsav_workout:
				Intent inte = new Intent(this, CrossFitrActivity.class);
				startActivity(inte);
				break;
				   
			// if user presses share on fb button, results will be shared on fb.			
			case R.id.button_results_share_workout_FB:
				//TODO: implement fb functionality
			    // if user presses this button, user will now go into the timer page.
				break;
					
		}
	}
	
	
}
