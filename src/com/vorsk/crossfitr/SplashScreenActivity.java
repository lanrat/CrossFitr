package com.vorsk.crossfitr;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * This is the splash screen activity for starting the app. 
 * @author Alice Nguyen
 *
 */

public class SplashScreenActivity extends Activity {
	protected int _splashTime = 3000; // time to display the splash screen in ms
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash_layout);
	 
	    TimerTask task = new TimerTask()
	    {
	    	@Override
	    	public void run() {
	    		finish();
	    		Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, CrossFitrActivity.class);
	    		startActivity(mainIntent);
	    	}
	    };
	    
	    Timer timer = new Timer();
	    timer.schedule(task, _splashTime);
	}
}
