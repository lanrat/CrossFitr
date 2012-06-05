package com.vorsk.crossfitr;

import java.util.ArrayList;

import com.vorsk.crossfitr.GirlsActivity.GirlsListHelper;
import com.vorsk.crossfitr.models.WODModel;
import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WodActivity extends Activity  implements OnItemClickListener
{
	private ListView wodView;
	protected ProgressDialog pd;
	ArrayAdapter<WorkoutRow> workoutrowList;
	private WodListHelper listAdapter;
	private static String TAG = "WODActivity";
	
	private TextView titleTextHeader1;
	private TextView titleTextHeader2;
	private Typeface font;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wod_workouts_list);
		
		font = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Thin.ttf");
		
		titleTextHeader1 = (TextView) findViewById(R.id.workouts_title);		
		titleTextHeader1.setTypeface(font);		
		titleTextHeader2 = (TextView) findViewById(R.id.wod_title);		
		titleTextHeader2.setTypeface(font);
		
		wodView = (ListView) findViewById(R.id.wod_workout_list);
		
		WODModel WODmodel = new WODModel(this);

		wodView.setOnItemClickListener(this);
		
		DownloadWOD downloadTask = new DownloadWOD(WODmodel,this);
		startLoadingScreen(downloadTask);
		downloadTask.execute(0);
	}
	
	protected void startLoadingScreen(final AsyncTask task){
		 pd = ProgressDialog.show(this, "Loading...", "Retrieving Workouts", true, true,
				 new DialogInterface.OnCancelListener(){
             public void onCancel(DialogInterface dialog) {
                 task.cancel(true);
                 finish();
             }
         }
		);
	}
	
	protected void stopLoadingScreen(){
		if (pd != null){
			pd.dismiss();
		}
	}
	
	/**
	 * ASync task for loading the RSS
	 * @author Ian
	 */
	 private class DownloadWOD extends AsyncTask<Integer, Integer, ArrayList<WorkoutRow>> {
		 WODModel model;
		 Activity context;
		 public DownloadWOD(WODModel model,Activity parent){
			 this.model = model;
			 this.context = parent;
		 }
	     protected ArrayList<WorkoutRow> doInBackground(Integer... models) {
	    	 model.fetchAll();
	         //publishProgress((int) ((i / (float) count) * 100));
	         return model.getWodRows();
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(ArrayList<WorkoutRow> results) {
			
			workoutrowList = new ArrayAdapter<WorkoutRow>(context,
					android.R.layout.simple_list_item_1, android.R.id.text1,results);
			stopLoadingScreen();
			
			listAdapter = new WodListHelper(getApplicationContext(), results);
			listAdapter.notifyDataSetChanged();
			
	 		wodView.setAdapter(listAdapter);

	     }
	 }

	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		//pass the ID of the workout into the WorkoutProfileActivity
		WorkoutRow workout = workoutrowList.getItem(position);
		
		//add the selected workout to the DB
		WorkoutModel model = new WorkoutModel(this);
		
		model.open();
		
		long entry_id = model.getIDFromName(workout.name);

		if (entry_id == -1){
			Log.d(TAG,"WOD not in DB, inserting");
			try {
				//entry_id = model.insert(workout);
				
				//Log.d(TAG,"WODTypeID: "+workout.record_type_id);
				entry_id = model.insert(workout.name, workout.description, (int)workout.workout_type_id,
														(int)workout.record_type_id, workout.record);
			} catch (SQLException e) {
				Log.e(TAG,"derp on wod insert");
				model.close();
				return;
			}

		}
		model.close();
		
		if (entry_id == -1){
			Log.e(TAG,"could not insert WOD into DB, unknown error");
			return;
		}
		
		Intent x = new Intent(this, WorkoutProfileActivity.class);
		x.putExtra("ID", entry_id); 
		startActivity(x);
	}
	
	public class WodListHelper extends BaseAdapter implements OnClickListener {

		private static final String tag = "WodListHelper";
		private final Context listContext;
		private ArrayList<WorkoutRow> arrayList;
		private ImageView listArrow;
		private TextView nameTView;
		private TextView descTView;
		private LayoutInflater inflater;

		public WodListHelper(Context _context, ArrayList<WorkoutRow> _data) {
			this.listContext = _context;
			this.arrayList = _data;
			inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public View getView(int index, View convertView, ViewGroup parent) {
			
			if (convertView == null)
				convertView = inflater
						.inflate(R.layout.custom_list_item, parent, false);

			listArrow = (ImageView) convertView.findViewById(R.id.image_arrow);
			listArrow.setOnClickListener(this);

			Log.d(tag, "arrayList.get(" + index + ").name : "
					+ arrayList.get(index).name);
			
			// workout name
			nameTView = (TextView) convertView
					.findViewById(R.id.string_nameofworkout);
			nameTView.setText(arrayList.get(index).name);
			nameTView.setTextColor(getResources().getColor(R.color.wod));	
			nameTView.setTypeface(font);		
			
			//workout description
			descTView = (TextView) convertView
					.findViewById(R.id.string_description);
			descTView.setText(arrayList.get(index).description);
			descTView.setTextColor(getResources().getColor(R.color.white));
			descTView.setSelected(true);
			descTView.setTypeface(font);

			return convertView;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		public String getItem(int index) {
			return arrayList.get(index).name;
		}

		public <T> T get(int arg0) {
			return (T) arrayList.get(arg0);
		}

		public long getItemId(int id) {
			return id;
		}

		public void onClick(View arg0) {

		}

	}	

}