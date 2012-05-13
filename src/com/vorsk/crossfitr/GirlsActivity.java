package com.vorsk.crossfitr;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GirlsActivity extends ListActivity
{
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);
		
		String[] GIRLS = new String[5];

		//setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, GIRLS));

		  ListView lv = getListView();
		  lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		      // When clicked, show a toast with the TextView text
		      //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
		      //    Toast.LENGTH_SHORT).show();
		    }
		  });
	}
	
}