package com.vorsk.crossfitr;

import java.util.ArrayList;

import com.vorsk.crossfitr.models.WODModel;
import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WodActivity extends Activity  implements OnItemClickListener
{
	private ListView listView;
	protected ProgressDialog pd;
	ArrayAdapter<WorkoutRow> adapter;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_list);
		
		listView = (ListView) findViewById(R.id.workout_list_view);
		
		WODModel WODmodel = new WODModel();

		/*adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1);

		listView.setAdapter(adapter);*/
		
		listView.setOnItemClickListener(this);
		
		new DownloadWOD(WODmodel,this).execute(0);
		
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
	 private class DownloadWOD extends AsyncTask<Integer, Integer, ArrayList<WorkoutRow>> {
		 WODModel model;
		 public DownloadWOD(WODModel model,Activity parent){
			 this.model = model;
			 pd = ProgressDialog.show(parent, "Loading...", "Retrieving Workouts", true, false);
		 }
	     protected ArrayList<WorkoutRow> doInBackground(Integer... models) {
	    	 model.fetch();
	         //publishProgress((int) ((i / (float) count) * 100));
	         return model.getWodRows();
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(ArrayList<WorkoutRow> result) {
	    	adapter = new ArrayAdapter<WorkoutRow>(getThis(),android.R.layout.simple_list_item_1,android.R.id.text1,result);
	 		pd.dismiss();
	 		listView.setAdapter(adapter);
	     }
	 }

	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		//pass the ID of the workout into the WorkoutProfileActivity
		WorkoutRow workout = adapter.getItem(position);
		
		//add the selected workout to the DB
		WorkoutModel model = new WorkoutModel(this);
		model.open();
		long entry_id;

		try {
			entry_id = model.insert(workout);
		} catch (SQLException e) {
			Log.e("WODActivity","derp on wod insert");
			return;
		}
		
		Intent x = new Intent(this, WorkoutProfileActivity.class);
		x.putExtra("ID", entry_id); 
		startActivity(x);
		
	}

}