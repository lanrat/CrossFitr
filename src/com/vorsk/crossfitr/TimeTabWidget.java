package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;


public class TimeTabWidget extends TabActivity{ // Resource object to get Drawables
	long id;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.time_tab_widget);
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;

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
	  	
	  	
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, TimerActivity.class);
	    intent.putExtra("ID", id);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("timer").setIndicator("Timer",
	                      res.getDrawable(R.drawable.tab_timer))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, StopwatchActivity.class);
	    intent.putExtra("ID", id);
	    spec = tabHost.newTabSpec("stopwatch").setIndicator("Stopwatch",
	                      res.getDrawable(R.drawable.tab_stopwatch))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TabataActivity.class);
	    intent.putExtra("ID", id);
	    spec = tabHost.newTabSpec("tabata").setIndicator("Tabata",
	                      res.getDrawable(R.drawable.tab_tabata))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	    model.close();
	}
}

	