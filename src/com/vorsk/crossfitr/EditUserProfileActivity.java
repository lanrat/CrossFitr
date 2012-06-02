package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.ProfileModel;

import android.app.Activity;
import android.content.Intent;
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
	ProfileModel model = new ProfileModel(this);
	
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile_form);
		model.open();
	

		// Save button
		View saveButton = findViewById(R.id.button_userprofile_form_save);
		saveButton.setOnClickListener(this);
		
		// Cancel button
		View cancelButton = findViewById(R.id.button_userprofile_form_cancel);
		cancelButton.setOnClickListener(this);
		
		// Name field
        nameTextField = (EditText) findViewById(R.id.user_name_field);
        nameTextField.setOnClickListener(this);
        nameTextField.setText(model.getByAttribute("name").value);        

        // Weight field
        weightTextField = (EditText) findViewById(R.id.user_weight_field);
        weightTextField.setOnClickListener(this);
        weightTextField.setText(model.getByAttribute("weight").value);

        
        //Goal Weight field
        goalWeightTextField = (EditText) findViewById(R.id.user_goal_weight_field);
        goalWeightTextField.setOnClickListener(this);
        goalWeightTextField.setText(model.getByAttribute("goal_weight").value);

        
        //Height field
        heightTextField = (EditText) findViewById(R.id.user_height_field);
        heightTextField.setOnClickListener(this);
        heightTextField.setText(model.getByAttribute("height").value);
        
        model.close();
	}

	public void onClick(View v) 
	{
		model.open();
		switch(v.getId())
		{
		case R.id.button_userprofile_form_save:
			long name_id = model.updateInsert("name", nameTextField.getText().toString());
			long height_id = model.updateInsert("height", heightTextField.getText().toString());
			long current_weight_id = model.updateInsert("weight", weightTextField.getText().toString());
			long goal_weight_id = model.updateInsert("goal_weight", goalWeightTextField.getText().toString());
			break;
		case R.id.button_userprofile_form_cancel:
			Intent u = new Intent(this, UserProfileActivity.class);
			startActivity(u);
			break;
		}
		model.close();
	}
}

