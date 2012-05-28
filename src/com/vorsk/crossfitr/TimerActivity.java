package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

public class TimerActivity extends Activity 
{	
    static final int NUMBER_DIALOG_ID = 0; // Dialog variable
    TextView mTimeDisplay, mElapsedTime;
    private int mHour, mMin, mSec;
    NumberPicker mNumberPicker;
    Button mStart, mPause, mReset;
    Time mTime = new Time();
    
    
	// Timer to update the elapsedTime display
    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2; 
	private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
        	//updateElapsedTime();
        	sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };
    
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_tab);

		mTimeDisplay = (TextView) findViewById(R.id.timer_label);
		Button mSelectTimer = (Button) findViewById(R.id.pickTime);
		
		mElapsedTime = (TextView)findViewById(R.id.ElapsedTime);
        
        mStart = (Button)findViewById(R.id.StartButton);
        mPause = (Button)findViewById(R.id.PauseButton);
        mReset = (Button)findViewById(R.id.ResetButton);
        
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    
		// Opens Dialog on click
		mSelectTimer.setOnClickListener(new View.OnClickListener()
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
		if(mHour == 0){
			MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.countdown_00);
			mediaPlayer.start();
		}
		mTimeDisplay.setText(new StringBuilder().append(mHour)
	    	.append(":").append(pad(mMin)).append(":").append(pad(mSec)));
	}
	
	public void updateElapsedTime() {
		//mElapsedTime.setText(formatElapsedTime(mTime.getElapsedTime()));
	}
	 
	private String formatElapsedTime(long now) {
		long hours=0, minutes=0, seconds=0, tenths=0;
		StringBuilder sb = new StringBuilder();

		if (now < 1000) {
			tenths = now / 100;
		} else if (now < 60000) {
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = now / 100;
		} else if (now < 3600000) {
			hours = now / 3600000;
			now -= hours * 3600000;
			minutes = now / 60000;
			now -= minutes * 60000;
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = now / 100;
		}
		
		if (hours > 0) {
			sb.append(hours).append(":")
				.append(formatDigits(minutes)).append(":")
				.append(formatDigits(seconds)).append(".")
				.append(tenths);
		} else {
			sb.append(formatDigits(minutes)).append(":")
			.append(formatDigits(seconds)).append(".")
			.append(tenths);
		}
		
		return sb.toString();
	}
	
	private String formatDigits(long num) {
		return (num < 10) ? "0" + num : new Long(num).toString();
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
