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

public class CustomActivity extends ListActivity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		String[] CUSTOM = new String[] { "CustomA", "CustomB", "CustomC",
				"CustomD", "CustomE" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, CUSTOM);

		setListAdapter(adapter);
	}
}