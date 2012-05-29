package com.vorsk.crossfitr;

import android.app.Activity;
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
	private TextView sElapsedTime;
	private Button sStart, sPause, sReset;
	private Time stopwatch = new Time();

	
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


        //startService(new Intent(this, StopwatchService.class));
        //bindStopwatchService();
        
        sElapsedTime = (TextView)findViewById(R.id.ElapsedTime);
        
        sStart = (Button)findViewById(R.id.StartButton);
        sPause = (Button)findViewById(R.id.PauseButton);
        sReset = (Button)findViewById(R.id.ResetButton);
        
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
   
    private void showPauseButton() {
    	Log.d(TAG, "showPauseLapButtons");
    	
    	sStart.setVisibility(View.GONE);
    	sReset.setVisibility(View.GONE);
    	sPause.setVisibility(View.VISIBLE);
    }
    
    private void showStartResetButtons() {
    	Log.d(TAG, "showStartResetButtons");

    	sStart.setVisibility(View.VISIBLE);
    	sReset.setVisibility(View.VISIBLE);
    	sPause.setVisibility(View.GONE);
    }
    
    public void onStartClicked(View v) {
    	Log.d(TAG, "start button clicked");
    	stopwatch.start();
    	
    	showPauseButton();
    }
    
    public void onPauseClicked(View v) {
    	Log.d(TAG, "pause button clicked");
    	stopwatch.stop();
    	
    	showStartResetButtons();
    }
    
    public void onResetClicked(View v) {
    	Log.d(TAG, "reset button clicked");
    	stopwatch.reset();
    }
    
    public void updateElapsedTime() {
   		sElapsedTime.setText(getFormattedElapsedTime());
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
	
	public String getFormattedElapsedTime() {
		return formatElapsedTime(getElapsedTime());
	}
	
	public long getElapsedTime() {
		return stopwatch.getElapsedTime();

	}
}
