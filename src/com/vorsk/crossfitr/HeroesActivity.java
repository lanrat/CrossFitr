package com.vorsk.crossfitr;

import android.app.Activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HeroesActivity extends ListActivity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		String[] HEROES = new String[] { "HeroA", "HeroB", "HeroC", "HeroD",
				"HeroE" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, HEROES);

		setListAdapter(adapter);
	}

	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
	}
}