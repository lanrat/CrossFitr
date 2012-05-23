package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.content.Context;

public class TimerActivity extends Activity 
{	
    static final int NUMBER_DIALOG_ID = 3; // Dialog variable

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_tab);

		Button mPickTime = (Button) findViewById(R.id.pickTime);
		
		// Opens Dialog on click
		mPickTime.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				showDialog(NUMBER_DIALOG_ID);
	        }
	    });
	}

	// Override creating the Dialog
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		return new NumberPickerDialog(this, 0, 0);
	}
}
