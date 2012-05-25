package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

public class TimerActivity extends Activity 
{	
    static final int NUMBER_DIALOG_ID = 0; // Dialog variable
    TextView mTimeDisplay;
    private int mHour, mMin, mSec;
    NumberPicker mNumberPicker;
    
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_tab);

		Button mPickTime = (Button) findViewById(R.id.pickTime);
		mTimeDisplay = (TextView) findViewById(R.id.timer_label);
		
		// Opens Dialog on click
		mPickTime.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				showDialog(NUMBER_DIALOG_ID);
	        }
	    });
	}
	
	private NumberPickerDialog.OnNumberSetListener mNumberSetListener =
			new NumberPickerDialog.OnNumberSetListener() {
				public void onNumberSet(int selectedHour, int selectedMin, int selectedSec) {
					mHour = selectedHour;
					mMin = selectedMin;
					mSec = selectedSec;
					updateDisplay();
				}

		    };

	private void updateDisplay() {
		mTimeDisplay.setText(new StringBuilder().append(mHour)
	    	.append(":").append(pad(mMin)).append(":").append(pad(mSec)));
	}
	
	/**
	 * Adds '0' in front of values less than 10
	 * @param c
	 * @return value with '0' 
	 */
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	
	// Override creating the Dialog
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		return new NumberPickerDialog(this, mNumberSetListener, 2, 0);
	}
}
