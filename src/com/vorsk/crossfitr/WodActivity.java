package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WODModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WodActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);

		
		WODModel model = new WODModel();

		ArrayAdapter<WorkoutRow> adapter = new ArrayAdapter<WorkoutRow>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,model.getWodRows() );

		ListView listView = (ListView) findViewById(R.id.workout_list_view);


		listView.setAdapter(adapter);
	}
	
	/*public void onListItemClick(ListView list, View view, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
	}*/

}