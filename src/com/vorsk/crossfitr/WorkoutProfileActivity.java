package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.WorkoutModel;
import com.vorsk.crossfitr.models.WorkoutRow;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WorkoutProfileActivity extends Activity implements OnClickListener 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		WorkoutModel model = new WorkoutModel(this);
		long id = getIntent().getLongExtra("ID", -1);
		if(id < 0)
		{
			startActivity(new Intent(this, CrossFitrActivity.class));
		}
		
		setContentView(R.layout.workout_profile);
		
		Log.v("VIEW", "ID = " + id);
		WorkoutRow row = model.getByID(id);
		
		TextView tvname = (TextView)findViewById(R.id.workout_profile_nameDB);
		TextView tvdesc = (TextView)findViewById(R.id.workout_profile_descDB);
		TextView tvrecordType = (TextView)findViewById(R.id.workout_profile_recordtypeDB); 
		TextView tvbestRecord = (TextView)findViewById(R.id.workout_profile_best_recordDB);
		
		tvname.setText(row.name);
		tvdesc.setText(row.description);
		tvrecordType.setText(String.valueOf(row.workout_type_id));//TODO: convert later after method
		tvbestRecord.setText(String.valueOf(row.record));
        // begin workout button
        View beginButton = findViewById(R.id.button_begin_workout);
        beginButton.setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		    // if user presses begin button, user will now go into the timer page.
			case R.id.button_begin_workout:
				Intent i = new Intent(this, TimeTabWidget.class);
				startActivity(i);
				break;
		}
	}
}
