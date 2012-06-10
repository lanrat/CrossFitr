package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class TimeTabWidget extends TabActivity{ // Resource object to get Drawables
	long id;
	TabHost tabhost;
	private WorkoutRow workout;
	int custom_score;
	TextView tvname, screenName;
	private Typeface font;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.time_tab_widget);
	    
	    Resources res = getResources(); // Resource object to get Drawables
	    tabhost = getTabHost(); // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;

	  	//get the id passed from previous activity (workout lists)
	  	id = getIntent().getLongExtra("workout_id", -1);
	  	//if ID is invalid, go back to home screen
	  	if(id < 0)
	  	{
	  		getParent().setResult(RESULT_CANCELED);
	  		finish();
	  	}
	  	
	  	WorkoutModel model = new WorkoutModel(this);
	  	model.open();
		workout = model.getByID(id);

	  	
	  	custom_score = getIntent().getIntExtra("workout_score", -1);
	  	if (custom_score <= 0) custom_score = -1;
	  	
	  	font = Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Thin.ttf");
	  	screenName = (TextView) findViewById(R.id.screenTitle);
		screenName.setTypeface(font);
		
	  	tvname = (TextView) findViewById(R.id.workout_profile_nameDB);
		tvname.setTypeface(font);
		tvname.setText(workout.name);
		
		if (model.getTypeName(workout.workout_type_id).equals("WOD"))
			tvname.setTextColor(res.getColor(R.color.wod));
		else if (model.getTypeName(workout.workout_type_id).equals("Hero"))
			tvname.setTextColor(res.getColor(R.color.heroes));
		else if (model.getTypeName(workout.workout_type_id).equals("Girl"))
			tvname.setTextColor(res.getColor(R.color.girls));
		else if(model.getTypeName(workout.workout_type_id).equals("Custom"))
			tvname.setTextColor(res.getColor(R.color.custom));
		model.close();
	  	
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, TimerActivity.class);
	    intent.putExtra("ID", id);
	    intent.putExtra("score", custom_score);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabhost.newTabSpec("timer").setIndicator("Timer",
	                      res.getDrawable(R.drawable.timer_tab_icons))
	                  .setContent(intent);
	    tabhost.addTab(spec);
	    tabhost.setEnabled(false);


	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, StopwatchActivity.class);
	    intent.putExtra("ID", id);
	    intent.putExtra("score", custom_score);
	    spec = tabhost.newTabSpec("stopwatch").setIndicator("Stopwatch",
	                      res.getDrawable(R.drawable.stopwatch_tab_icons))
	                  .setContent(intent);
	    tabhost.addTab(spec);

	    intent = new Intent().setClass(this, TabataActivity.class);
	    intent.putExtra("ID", id);
	    intent.putExtra("score", custom_score);
	    spec = tabhost.newTabSpec("tabata").setIndicator("Tabata",
	                      res.getDrawable(R.drawable.tabata_tab_icons))
	                  .setContent(intent);
	    tabhost.addTab(spec);

	    tabhost.setCurrentTab(1);
	}
}

	
