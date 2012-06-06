package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;
import com.vorsk.crossfitr.models.WorkoutSessionModel;
import com.vorsk.crossfitr.models.WorkoutSessionRow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Creates and displays the Results view after completing a workout.
 * 
 * When invoking this activity, the caller must provide the relevant
 * workout session ID via the Intent. The results will be displayed for
 * this session.
 * 
 * @requires Intent->session_id
 * @author Vivek
 */
public class ResultsActivity extends Activity implements OnClickListener
{
	private long session_id;
	private EditText commentTextField;
	private InputMethodManager keyControl;
	private WorkoutRow workout;
	private Typeface font;
	TextView screenName, tvname, tvdesc, tvbestRecord, tvscore;
	
	/**
	 * Automatically ends this activity and returns control to the caller
	 * This should be called if there was insufficient privelages to invoke
	 * this activity.
	 * 
	 * @param tag A category for the reason
	 * @param reason Reason that we must stop this activity (for logging)
	 */
	private void reject(String tag, String reason)
	{
		Log.v(tag, reason);
		finish();
	}
	
	/**
	 * Performs all the validation for session_id
	 * 
	 * @return session associated with the ID
	 */
	private WorkoutSessionRow validateAccess()
	{
		if (getIntent() == null) {
			reject("Page_Requirement", "No intent provided");
		}
		
		session_id = getIntent().getLongExtra("session_id", 0L);
		if (session_id == 0L) {
			reject("Page_Requirement", "No session id provided");
		}
		
		WorkoutSessionModel wsmodel = new WorkoutSessionModel(this);
		wsmodel.open();
		WorkoutSessionRow session = wsmodel.getByID(session_id);
		if (session == null) {
			reject("Page_Requirement", "invalid session id was provided");
		}
		wsmodel.close();
		
		return session;
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		keyControl = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		WorkoutSessionRow session = validateAccess();
		
		// Get the relevant Workout performed
		WorkoutModel wmodel = new WorkoutModel(this);
		wmodel.open();
		workout = wmodel.getByID(session.workout_id);
		if (workout == null) 
		{
			Log.e("DB_Inconsistency", "Invalid workout id on session");
			finish();
		}
		
		// Create the initial view objects
		setContentView(R.layout.workout_results);
		
		//TextView objects

		font = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Thin.ttf");
		screenName = (TextView) findViewById(R.id.screenTitle);
		screenName.setTypeface(font);
		tvname = (TextView) findViewById(R.id.workout_results_nameDB);
		tvname.setTypeface(font);
		tvbestRecord = (TextView) findViewById(R.id.workout_results_best_recordDB);
		tvbestRecord.setTypeface(font);
		tvscore = (TextView) findViewById(R.id.workout_results_score);
		tvscore.setTypeface(font);
		tvdesc = (TextView) findViewById(R.id.workout_results_descDB);
		tvdesc.setTypeface(font);
		
		//set the text of the TextView objects from the data retrieved from the DB
		Resources res = getResources();
		tvname.setText(workout.name);
		if (wmodel.getTypeName(workout.workout_type_id).equals("WOD"))
			tvname.setTextColor(res.getColor(R.color.wod));
		else if (wmodel.getTypeName(workout.workout_type_id).equals("Hero"))
			tvname.setTextColor(res.getColor(R.color.heroes));
		else if (wmodel.getTypeName(workout.workout_type_id).equals("Girl"))
			tvname.setTextColor(res.getColor(R.color.girls));
		else if(wmodel.getTypeName(workout.workout_type_id).equals("Custom"))
			tvname.setTextColor(res.getColor(R.color.custom));
		tvdesc.setText(workout.description);
  		tvbestRecord.setText("Personal Record: "+StopwatchActivity.formatElapsedTime(Long.parseLong(String.valueOf(workout.record))));
  		tvscore.setText("Your Score: "+StopwatchActivity.formatElapsedTime(Long.parseLong(String.valueOf(session.score))));
		
		wmodel.close();
		
		// Get views
		View btn_save = findViewById(R.id.button_results_save_workout);
        ((TextView) btn_save).setTypeface(font);
        btn_save.setOnClickListener(this);
        
		View btn_nosave = findViewById(R.id.button_results_dontsave_workout);
        ((TextView) btn_nosave).setTypeface(font);
        btn_nosave.setOnClickListener(this);
        
		View btn_sharefb = findViewById(R.id.button_results_share_workout_FB);
        ((TextView) btn_sharefb).setTypeface(font);
        btn_sharefb.setOnClickListener(this);
        
		//edittext handler
		commentTextField = (EditText) findViewById(R.id.results_comment_edittext_add);
		commentTextField.setOnClickListener(this);

		// Set handlers
		btn_save.setOnClickListener(this);
		btn_nosave.setOnClickListener(this);
		btn_sharefb.setOnClickListener(this);
	}
	
	//method to hide the keyboard
	private void hideKeyboard(EditText eBox) 
	{
		keyControl.hideSoftInputFromWindow(eBox.getWindowToken(), 0);
	}
	
	/**
	 * Handles all the button clicks
	 */
	public void onClick(View v)
	{
		WorkoutSessionModel model = new WorkoutSessionModel(this);
		Intent intent;
		
		switch(v.getId())
		{
			// if user presses save and end button button, will go back to home screen after saving.
			case R.id.button_results_save_workout:
				model.open();
				model.editComment(session_id,
						commentTextField.getText().toString());
				model.close();
				finish();
				
				//close all activity except homepage activity
				intent  = new Intent(getBaseContext(), CrossFitrActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        
                startActivity(intent);

				break;
			// if user presses dont save button, go back to home screen.
			case R.id.button_results_dontsave_workout:
				model.open();
				model.delete(session_id);
				model.close();
				finish();
				
				//close all activities except homepage
				intent  = new Intent(getBaseContext(), CrossFitrActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        
                startActivity(intent);
                
				break;
				   
			// if user presses share on fb button, results will be shared on fb.			
			case R.id.button_results_share_workout_FB:
				//get the string to share:
				model.open();
				WorkoutSessionRow row = model.getByID(session_id);
				model.close();
				
				//share functionality
			    // if user presses this button, user will now go into the timer page.
        	    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(android.content.Intent.EXTRA_SUBJECT, getText(R.string.app_name));
                share.putExtra(android.content.Intent.EXTRA_TEXT, "I just used "+getText(R.string.app_name)+
                		" to complete "+workout.name+" with score: "+row.score+"!" );
                startActivity(share);
                
				break;
			/*	
			//should close keyboard if clicks on background
			case R.id.results_background:
				hideKeyboard(commentTextField);
				break;
		    */
		}
	}
	
}
