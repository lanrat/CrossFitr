package com.vorsk.crossfitr;

import java.util.ArrayList;

import com.vorsk.crossfitr.models.WODModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WodActivity extends Activity 
{
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);
		
		listView = (ListView) findViewById(R.id.workout_list_view);
		
		WODModel model = new WODModel();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1);
		adapter.add("Loading...");

		listView.setAdapter(adapter);
		
		new DownloadWOD().execute(model);
	}
	
	/**
	 * This is a horrible method that I feel bad about writing
	 * I'm sorry, I really am
	 * @return this
	 */
	protected Activity getThis(){
		return this;
	}
	
	/*public void onListItemClick(ListView list, View view, int position, long id) {
		String item = (String) listView.getAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
	}*/
	
	/**
	 * ASync task for loading the RSS
	 * @author Ian
	 */
	 private class DownloadWOD extends AsyncTask<WODModel, Integer, ArrayList<WorkoutRow>> {
	     protected ArrayList<WorkoutRow> doInBackground(WODModel... models) {
	    	 models[0].fetch();
	         //publishProgress((int) ((i / (float) count) * 100));
	         return models[0].getWodRows();
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(ArrayList<WorkoutRow> result) {
	 		ArrayAdapter<WorkoutRow> adapter = new ArrayAdapter<WorkoutRow>(getThis(),android.R.layout.simple_list_item_1,android.R.id.text1,result);
	    	listView.setAdapter(adapter);
	     }
	 }

}