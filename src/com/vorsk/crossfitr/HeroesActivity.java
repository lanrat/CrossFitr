package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class HeroesActivity extends ListActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String[] HEROES = new String[] { "HeroA", "HeroB", "HeroC", "HeroD", "HeroE" }; 
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, HEROES);
		
		setListAdapter(adapter);	
	}
}