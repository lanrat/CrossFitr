package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class HeroesActivity extends Activity
{
	static final String[] heroes = { "captain", "hulk", "ironmask" }; 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);
		
		
	}
}