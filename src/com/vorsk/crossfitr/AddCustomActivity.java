package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.EventLogTags.Description;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCustomActivity extends Activity implements OnClickListener 
{
	private EditText workoutTextField;
	private EditText nameTextField;
	Spinner workoutTypeDropDown;
	Spinner recordTypeDropDown; 
	WorkoutModel model = new WorkoutModel(this);
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_form);

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
        workoutTypeDropDown = (Spinner) findViewById(R.id.workout_form_workouttype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.workouttype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeDropDown.setAdapter(adapter);
        workoutTypeDropDown.setOnItemSelectedListener(new MyOnItemSelectedListener());

        
        // drop down menu for the record type to be added
        recordTypeDropDown = (Spinner) findViewById(R.id.workout_form_recordtype_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.recordtype_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recordTypeDropDown.setAdapter(adapter2);
        recordTypeDropDown.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}

	// called by the onClickListener
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		    // if user clicks the save button
			case R.id.button_workout_form_save:
				if(this.validateForm() == true)
				{
					model.open();
					//TODO: uncomment after adding in spinners correctly, change type and rec_type params
					/*model.insert(nameTextField.getText(), workoutTextField.getText(),
								 type, rec_type);*/
					model.close();
					Intent i = new Intent(this, CustomActivity.class);
					startActivity(i);
				}
				else
				{
					//TODO: error prompt
				}
				
			break;
			
			// user clicks the save and start workout button
			case R.id.button_workout_form_start:				
				if(this.validateForm() == true)
				{
					model.open();
					//TODO: uncomment after adding in spinners correctly, change type and rec_type params
					/*model.insert(nameTextField.getText(), workoutTextField.getText(),
								 type, rec_type);*/
					model.close();
					
					//TODO: uncomment below after WorkoutsProfileActivity has been created.
					/*Intent i = new Intent(this, WorkoutsProfileActivity.class);
					startActivity(i);*/
				}
				else
				{
					//TODO: error prompt
				}
			break;
		}
	}
	
	// listener for the spinner object
	public class MyOnItemSelectedListener implements OnItemSelectedListener 
	{
	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) 
	    {
	      // TODO: handle the different cases selected
	    }

	    public void onNothingSelected(AdapterView parent) 
	    {
	      // Do nothing.
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
		
		//TODO: add more two more validations for the dropdown menus.
		return isValidForm;
	}
}

