package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends Activity {
	private static String TAG = "StopwatchActivity";
	// View elements in stopwatch.xml
	private TextView sElapsedTime, mWorkoutDescription;
	private Button sStart, sStop, sReset;
	private Time stopwatch = new Time();
	long id;


	// Timer to update the elapsedTime display
    private final long mFrequency = 100;    // milliseconds
    private final int TICK_WHAT = 2; 
	private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
        	updateElapsedTime();
        	sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.stopwatch_tab);

        //create model object
	    WorkoutModel model = new WorkoutModel(this);
	  	//open model to put data into database
	  	model.open();
	  	//get the id passed from previous activity (workout lists)
	  	id = getIntent().getLongExtra("ID", -1);
	  	//if ID is invalid, go back to home screen
	  	if(id < 0)
	  	{
	  		startActivity(new Intent(this, CrossFitrActivity.class));
	  	}

        //startService(new Intent(this, StopwatchService.class));
        //bindStopwatchService();

	  	WorkoutRow row = model.getByID(id);
        
        sElapsedTime = (TextView)findViewById(R.id.ElapsedTime);
        mWorkoutDescription = (TextView)findViewById(R.id.workout_des_time);
        
        sStart = (Button)findViewById(R.id.StartButton);
        sStop = (Button)findViewById(R.id.StopButton);
        sReset = (Button)findViewById(R.id.ResetButton);
        
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
        

        mWorkoutDescription.setText(row.description);

        
        model.close();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
   
    private void showStopButton() {
    	Log.d(TAG, "showPauseLapButtons");
    	
    	sStart.setVisibility(View.GONE);
    	sReset.setVisibility(View.GONE);
    	sStop.setVisibility(View.VISIBLE);
    }
    
    private void showStartResetButtons() {
    	Log.d(TAG, "showStartResetButtons");

    	sStart.setVisibility(View.VISIBLE);
    	sReset.setVisibility(View.VISIBLE);
    	sStop.setVisibility(View.GONE);
    }
    
    public void onStartClicked(View v) {
    	Log.d(TAG, "start button clicked");
    	stopwatch.start();
    	((TimeTabWidget) getParent()).getTabHost().getTabWidget().getChildTabViewAt(0).setEnabled(false);
		((TimeTabWidget) getParent()).getTabHost().getTabWidget().getChildTabViewAt(2).setEnabled(false);
    	showStopButton();
    }
    
    public void onStopClicked(View v) {
    	Log.d(TAG, "stop button clicked");
    	stopwatch.stop();
    	((TimeTabWidget) getParent()).getTabHost().getTabWidget().getChildTabViewAt(0).setEnabled(true);
		((TimeTabWidget) getParent()).getTabHost().getTabWidget().getChildTabViewAt(2).setEnabled(true);
    	showStartResetButtons();
    }
    
    public void onResetClicked(View v) {
    	Log.d(TAG, "reset button clicked");
    	stopwatch.reset();
    }
    
    public void onFinishClicked(View v) {
		Intent result = new Intent();
		result.putExtra("time", getElapsedTime());
		getParent().setResult(RESULT_OK, result);
		finish();
	}
    
    public void updateElapsedTime() {
   		sElapsedTime.setText(getFormattedElapsedTime());
    }
    
	public static String formatElapsedTime(long now) {
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

	private static String formatDigits(long num) {
		return (num < 10) ? "0" + num : new Long(num).toString();
	}

	public String getFormattedElapsedTime() {
		return formatElapsedTime(getElapsedTime());
	}

	public long getElapsedTime() {
		return stopwatch.getElapsedTime();

	}
}