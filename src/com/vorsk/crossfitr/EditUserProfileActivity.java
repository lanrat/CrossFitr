package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class EditUserProfileActivity extends Activity implements OnClickListener 
{
	
	private EditText nameTextField;
	private EditText weightTextField;
	private EditText heightTextField;
	private EditText goalWeightTextField;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile_form);
		
		// Save button
		View saveButton = findViewById(R.id.button_userprofile_form_save);
		saveButton.setOnClickListener(this);
		
		// Cancel button
		View cancelButton = findViewById(R.id.button_userprofile_form_cancel);
		saveButton.setOnClickListener(this);
		
		// Name field
        nameTextField = (EditText) findViewById(R.id.user_name_field);
        nameTextField.setOnClickListener(this);

        // Weight field
        weightTextField = (EditText) findViewById(R.id.user_weight_field);
        weightTextField.setOnClickListener(this);
        
        //Goal Weight field
        goalWeightTextField = (EditText) findViewById(R.id.user_goal_weight_field);
        goalWeightTextField.setOnClickListener(this);
        
        //Height field
        heightTextField = (EditText) findViewById(R.id.user_height_field);
        heightTextField.setOnClickListener(this);
	}

	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub

	}
}