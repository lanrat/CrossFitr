package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WODModel;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WodActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);

		ListView listView = (ListView) findViewById(R.id.workout_list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				new WODModel().list);

		listView.setAdapter(adapter);
	}

}