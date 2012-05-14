package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.EventLogTags.Description;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddCustomActivity extends Activity implements OnClickListener 
{
	private EditText workoutTextField;
	private EditText nameTextField;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		// button to save
		View saveButton = findViewById(R.id.button_workout_form_save);
		saveButton.setOnClickListener(this);

		// button to save and start workout
		View saveAndStartButton = findViewById(R.id.button_workout_form_start);
		saveAndStartButton.setOnClickListener(this);
		
        // text field for the workout discription to be added
        workoutTextField = (EditText) findViewById(R.id.discription_edittext_add);
        workoutTextField.setOnClickListener(this);
        
        // text field for the workout name to be added
        nameTextField = (EditText) findViewById(R.id.nameofworkout_edittext_add);
        nameTextField.setOnClickListener(this);
        
        // drop down menu for the workout types to be added
        View workoutTypeDropDown = findViewById(R.id.workout_form_workouttype_spinner);
        workoutTypeDropDown.setOnClickListener(this);
        
        // drop down menu for the record type to be added
        View recordTypeDropDown = findViewById(R.id.workout_form_recordtype_spinner);
        recordTypeDropDown.setOnClickListener(this);

		setContentView(R.layout.workout_form);
	}

	public void onClick(View v) 
	{
		switch(v.getId())
		{
			case R.id.button_workout_form_save:
				if(this.validateForm() == true)
				{
					
				}
				
			break;
				case R.id.button_workout_form_start:				
				if(this.validateForm() == true)
				{
					
				}
					break;
		}
	}
	
	// method to make sure that input is valid, if false the save buttons should
	// do nothing and prompt user to fill in all forms
	// if true, buttons execute
	private boolean validateForm()
	{
		// assume form is valid
		boolean isValidForm = true;
		// makes sure user inputed something in the name field
		if(nameTextField.getText().length() <= 0)
		{
			isValidForm = false;
		}
		// makes sure user inputed something in the description field
		if(workoutTextField.getText().length() <= 0)
		{
			isValidForm = false;
		}
		//TODO: add more two more validations for the dropdown.
		return isValidForm;
	}
}

