package com.vorsk.crossfitr;

import java.math.BigDecimal;

import com.vorsk.crossfitr.models.ProfileModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



public class UserProfileActivity extends Activity implements OnClickListener 
{
	
	ProfileModel model = new ProfileModel(this);
	
	private TextView userNameText;
	private TextView userBMIText;
	private TextView userWeightText;
	private TextView userGoalWeightText;
	private TextView userHeightText;
	private TextView userTotalWorkoutsText;
	private TextView userLastWorkoutText;
	private TextView userTotalAchievementsText;

	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile);
		
		// Displaying user data
		model.open();
		
		// Name
		if(model.getByAttribute("name") != null){
			userNameText = (TextView) findViewById(R.id.user_name);
			userNameText.setText(this.getString(R.string.user_name) + " " + model.getByAttribute("name").value);
		}
		
		// BMI
		userBMIText = (TextView) findViewById(R.id.user_bmi);
		if(model.calculateBMI() != BigDecimal.valueOf(-1))
			userBMIText.setText(this.getString(R.string.user_bmi) + " " + model.calculateBMI().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		// Current Weight
		if(model.getByAttribute("current_weight") != null){
			userWeightText = (TextView) findViewById(R.id.user_weight);
			userWeightText.setText(this.getString(R.string.user_weight) + " " + model.getByAttribute("weight").value);
		}
		
		// Goal Weight
		if(model.getByAttribute("goal_weight") != null){
			userGoalWeightText = (TextView) findViewById(R.id.user_goal_weight);
			userGoalWeightText.setText(this.getString(R.string.user_goal_weight) + " " + model.getByAttribute("goal_weight").value);
		}
		
		// Current Height
		if(model.getByAttribute("current_height") != null){
			userHeightText = (TextView) findViewById(R.id.user_height);
			userHeightText.setText(this.getString(R.string.user_height) + " " + model.getByAttribute("height").value);
		}
		
		// Total Workouts
		if(model.getByAttribute("total_workouts") != null){
			userTotalWorkoutsText = (TextView) findViewById(R.id.user_total_workouts);
			userTotalWorkoutsText.setText(this.getString(R.string.user_total_workouts) + " " + model.getByAttribute("total_workouts").value);
		}
		
		// Last Workout
		if(model.getByAttribute("last_workout") != null){
			userLastWorkoutText = (TextView) findViewById(R.id.user_last_workout);
			userLastWorkoutText.setText(this.getString(R.string.user_last_workout) + " " + model.getByAttribute("last_workout").value);
		}
		
		// Total Achievements
		if(model.getByAttribute("total_achievements") != null){
			userTotalAchievementsText = (TextView) findViewById(R.id.user_total_achievements);
			userLastWorkoutText.setText(this.getString(R.string.user_total_achievements) + " " + model.getByAttribute("total_achievements").value);
		}
		
		// Edit Profile button
		View user_profile_button = findViewById(R.id.edit_profile_button);
		user_profile_button.setOnClickListener(this);
		
		// Temporary button to access and debug the timer
		View open_timer_button = findViewById(R.id.open_timer_button);
		open_timer_button.setOnClickListener(this);
		
		// Injuries button
		View injuries_button = findViewById(R.id.injuries_button);
		injuries_button.setOnClickListener(this);
		
		// Achievements button
		View achievements_button = findViewById(R.id.achievements_button);
		achievements_button.setOnClickListener(this);
		
		// Results button
		View results_button = findViewById(R.id.open_results_button);
		results_button.setOnClickListener(this);
		model.close();
	}
	
	public void onResume()
	{
		super.onResume();
		
		// Displaying user data
		model.open();

		// Name
		if(model.getByAttribute("name") != null){
			userNameText = (TextView) findViewById(R.id.user_name);
			userNameText.setText(this.getString(R.string.user_name) + " " + model.getByAttribute("name").value);
		}
		
		// BMI
		userBMIText = (TextView) findViewById(R.id.user_bmi);
		if(model.calculateBMI() != BigDecimal.valueOf(-1))
			userBMIText.setText(this.getString(R.string.user_bmi) + " " + model.calculateBMI().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		
		// Weight
		if(model.getByAttribute("weight") != null){
			userWeightText = (TextView) findViewById(R.id.user_weight);
			userWeightText.setText(this.getString(R.string.user_weight) + " " + model.getByAttribute("weight").value);
		}
		
		// Goal Weight
		if(model.getByAttribute("goal_weight") != null){
			userGoalWeightText = (TextView) findViewById(R.id.user_goal_weight);
			userGoalWeightText.setText(this.getString(R.string.user_goal_weight) + " " + model.getByAttribute("goal_weight").value);
		}
		
		// Height
		if(model.getByAttribute("height") != null){
			userHeightText = (TextView) findViewById(R.id.user_height);
			userHeightText.setText(this.getString(R.string.user_height) + " " + model.getByAttribute("height").value);
		}
		
		// Total Workouts
		if(model.getByAttribute("total_workouts") != null){
			userTotalWorkoutsText = (TextView) findViewById(R.id.user_total_workouts);
			userTotalWorkoutsText.setText(this.getString(R.string.user_total_workouts) + " " + model.getByAttribute("total_workouts").value);
		}
		
		// Last Workout
		if(model.getByAttribute("last_workout") != null){
			userLastWorkoutText = (TextView) findViewById(R.id.user_last_workout);
			userLastWorkoutText.setText(this.getString(R.string.user_last_workout) + " " + model.getByAttribute("last_workout").value);
		}
		
		// Total Achievements
		if(model.getByAttribute("total_achievements") != null){
			userTotalAchievementsText = (TextView) findViewById(R.id.user_total_achievements);
			userLastWorkoutText.setText(this.getString(R.string.user_total_achievements) + " " + model.getByAttribute("total_achievements").value);
		}
				
		model.close();
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		case R.id.edit_profile_button:
			Intent u = new Intent(this, EditUserProfileActivity.class);
			startActivity(u);
			break;
		case R.id.open_timer_button:
			Intent t = new Intent(this, TimeTabWidget.class);
			startActivity(t);
			break;
		case R.id.open_results_button:
			Intent x = new Intent(this, ResultsActivity.class);
			startActivity(x);
		case R.id.injuries_button:
			// TODO add injuries intent
			break;
		case R.id.achievements_button:
			// TODO add achievements intent
			break;
		}
	}
}
