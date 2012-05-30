package com.vorsk.crossfitr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TabataActivity extends Activity {
	private static final int TOTAL_TIME = 30000 * 8;
	private static String TAG = "StopwatchActivity";
	// View elements in stopwatch.xml
	private TextView t_elapsedTime;
	private Button t_start;
	private Button t_stop;
	private Button t_reset;
	private Time tabata = new Time();
	private boolean newStart;

	// Timer to update the elapsedTime display
	private final long mFrequency = 100; // milliseconds
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

		setContentView(R.layout.tabata_tab);
		newStart = true;

		t_elapsedTime = (TextView) findViewById(R.id.ElapsedTime);

		t_start = (Button) findViewById(R.id.StartButton);
		t_stop = (Button) findViewById(R.id.StopButton);
		t_reset = (Button) findViewById(R.id.ResetButton);

		mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT),
				mFrequency);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void showStopButton() {
		Log.d(TAG, "showPauseLapButtons");

		t_start.setVisibility(View.GONE);
		t_reset.setVisibility(View.GONE);
		t_stop.setVisibility(View.VISIBLE);
	}

	private void showStartResetButtons() {
		Log.d(TAG, "showStartResetButtons");

		t_start.setVisibility(View.VISIBLE);
		t_reset.setVisibility(View.VISIBLE);
		t_stop.setVisibility(View.GONE);
	}

	public void onStartClicked(View v) {
		Log.d(TAG, "start button clicked");
		tabata.start();
		newStart = false;
		showStopButton();
	}

	public void onStopClicked(View v) {
		Log.d(TAG, "stop button clicked");
		newStart = false;
		tabata.stop();
		showStartResetButtons();
	}

	public void onResetClicked(View v) {
		Log.d(TAG, "reset button clicked");
		newStart = true;
		tabata.reset();
	}
	
	public void onFinishedClicked(View v){
		Intent i = new Intent(this, ResultsActivity.class);
		startActivity(i);
	}

	private void endTabata() {
		newStart = true;
		tabata.reset();
		this.showStartResetButtons();
		//TODO: end alarm sound and popup??
	}

	public void updateElapsedTime() {
		t_elapsedTime.setText(getFormattedElapsedTime());
	}

	private String formatElapsedTime(long now, int set) {
		long seconds = 0, tenths = 0;
		StringBuilder sb = new StringBuilder();

		if(newStart){
			now = 20000;			
		}
		
		if (now < 1000) {
			tenths = now / 100;
		} else if (now < 60000) {
			seconds = now / 1000;
			now -= seconds * 1000;
			tenths = now / 100;
		}


		sb.append("SET : ").append(set).append("\n").append(formatDigits(seconds)).append(".").append(tenths);

		return sb.toString();
	}

	private String formatDigits(long num) {
		return (num < 10) ? "0" + num : new Long(num).toString();
	}

	public String getFormattedElapsedTime() {
		long time = tabata.getElapsedTime();
		
		int set = 1 + ((int)time / 30000);
		long diff = TOTAL_TIME - time;
		long remain = diff % 30000;
		
		int green = Color.GREEN;
		int red = Color.RED;
		
		//reset at end of set 8 workout. no last 10 sec break
		if(diff <= 10000){
			set = 1;
			this.endTabata();
		}
		if(remain > 10000 ){
			this.setActivityBackgroundColor(green);
			return formatElapsedTime(20000 - (time % 30000), set);
		}else if(remain == 10000){
			//TODO: beep, change color(green or red)
			return formatElapsedTime(0, set);
		}else{
			this.setActivityBackgroundColor(red);
			return formatElapsedTime(30000 - (time % 30000), set);
		}
	}
	
	public void setActivityBackgroundColor(int color){
	    View view = this.getWindow().getDecorView();
	    view.setBackgroundColor(color);
	}

}
