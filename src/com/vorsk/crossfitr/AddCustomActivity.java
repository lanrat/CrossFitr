package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AddCustomActivity extends Activity implements OnClickListener 
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//button to save
		View saveButton = findViewById(R.id.button_workout_form_save);
		saveButton.setOnClickListener(this);
		
		//button to save and start workout
		View saveAndStartButton = findViewById(R.id.button_workout_form_start);
		saveAndStartButton.setOnClickListener(this);
		
        // text field for the workout discription to be added
        View workoutTextField = findViewById(R.id.discription_edittext_add);
        workoutTextField.setOnClickListener(this);
        
        // text field for the workout name to be added
        View nameTextField = findViewById(R.id.nameofworkout_edittext_add);
        nameTextField.setOnClickListener(this);
        
        // drop down menu for the workout types to be added
        View workoutTypeDropDown = findViewById(R.id.workout_form_workouttype_spinner);
        workoutTypeDropDown.setOnClickListener(this);
        
        // drop down menu for the record type to be added
        View recordTypeDropDown = findViewById(R.id.workout_form_recordtype_spinner);
        recordTypeDropDown.setOnClickListener(this);

		setContentView(R.layout.workout_form);
	}

	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.button_workout_form_save:
				
				break;
			case R.id.button_workout_form_start:
				
				break;
		}
		
	}
}
