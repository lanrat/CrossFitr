package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class EditUserProfileActivity extends Activity implements OnClickListener 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile_form);
		
		// button to save
		View saveButton = findViewById(R.id.button_userprofile_form_save);
		saveButton.setOnClickListener(this);

	}

	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub

	}
}