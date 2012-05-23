package com.vorsk.crossfitr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class UserProfileActivity extends Activity implements OnClickListener 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile);

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
		injuries_button.setOnClickListener(this);
		
	}

	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
		case R.id.edit_profile_button:
			Intent u = new Intent(this, EditUserProfileActivity.class);
			startActivity(u);
			break;
		case R.id.open_timer_button:
			Intent t = new Intent(this, TimerTabWidget.class);
			startActivity(t);
			break;
		case R.id.injuries_button:
			// TODO add injuries intent
			break;
		case R.id.achievements_button:
			// TODO add achievements intent
			break;
		}
	}
}
