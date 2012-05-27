package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ResultsActivity extends Activity implements OnClickListener
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		View saveButton = findViewById(R.id.button_results_sav_workout);
		saveButton.setOnClickListener(this);
		View dontSaveButton = findViewById(R.id.button_results_dontsav_workout);
		dontSaveButton.setOnClickListener(this);
		View shareOnFB = findViewById(R.id.button_results_share_workout_FB);
		shareOnFB.setOnClickListener(this);
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
