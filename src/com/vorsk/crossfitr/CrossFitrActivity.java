package com.vorsk.crossfitr;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import com.vorsk.crossfitr.models.AchievementModel;
import com.vorsk.crossfitr.models.ProfileModel;
import com.vorsk.crossfitr.models.WorkoutSessionModel;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CrossFitrActivity extends Activity implements OnClickListener {
	private TextView workoutsText;
	private TextView calendarText;
	private TextView profileText;
	private TextView numOfWorkouts;
	private TextView lastWorkouts;
	private TextView numOfAchievments;

	private TextView statusDisplay1;
	private TextView statusDisplay2;
	private TextView statusDisplay3;

	private ImageView userPic;

	private File file;
	private Typeface font;

	WorkoutSessionModel sessionModel = new WorkoutSessionModel(this);
	ProfileModel profileModel = new ProfileModel(this);
	AchievementModel achievementModel = new AchievementModel(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Typeface harabara = Typeface.createFromAsset(this.getAssets(),
				"fonts/Harabara.ttf");
		// Custom title bar
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView title = (TextView) findViewById(R.id.title);
		title.setTypeface(harabara);
		title.setText("crossfitr");

	}

	@Override
	public void onResume() {
		super.onResume();

		font = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Thin.ttf");

		// User photo
		file = new File(Environment.getExternalStorageDirectory(),
				"profile.png");
		userPic = (ImageView) this.findViewById(R.id.main_button_userpic);
		Bitmap bMap = BitmapFactory.decodeFile(file.toString());
		if (bMap != null) {
			userPic.setImageBitmap(bMap);
		}

		// workouts button
		View workoutButton = findViewById(R.id.main_button_workouts);
		workoutButton.setOnClickListener(this);
		workoutsText = (TextView) findViewById(R.id.main_button_workouts);
		workoutsText.setTypeface(font);

		// calendar button
		View calendarButton = findViewById(R.id.main_button_calendar);
		calendarButton.setOnClickListener(this);
		calendarText = (TextView) findViewById(R.id.main_button_calendar);
		calendarText.setTypeface(font);

		// profile button
		View profileButton = findViewById(R.id.main_button_profile);
		profileButton.setOnClickListener(this);
		profileText = (TextView) findViewById(R.id.main_button_profile);
		profileText.setTypeface(font);
		profileText.setGravity(Gravity.LEFT);

		// Building String
		profileModel.open();

		// Name Section
		String profileDetails = "  Name: ";
		if (profileModel.getByAttribute("name") != null) {
			profileDetails += profileModel.getByAttribute("name").value;
		}

		// BMI Section
		profileDetails += "\n  BMI: ";
		if ((profileModel.getByAttribute("weight") != null)
				&& (profileModel.getByAttribute("height") != null)) {
			profileDetails += profileModel.calculateBMI()
					.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}

		// Current Weight Section
		profileDetails += "\n  Current Weight: ";
		if (profileModel.getByAttribute("weight") != null) {
			profileDetails += profileModel.getByAttribute("weight").value
					+ " lbs";
		}

		// Goal Weight Section
		profileDetails += "\n  Goal Weight: ";
		if (profileModel.getByAttribute("goal_weight") != null) {
			profileDetails += profileModel.getByAttribute("goal_weight").value
					+ " lbs";
		}

		if ((profileModel.getByAttribute("name") == null)
				&& (profileModel.getByAttribute("weight") == null)
				&& (profileModel.getByAttribute("goal_weight") == null)) {
			profileDetails = " Press here to\n create your new\n profile!";
		}

		profileText.setText(profileDetails);

		// Status Displays
		statusDisplay1 = (TextView) findViewById(R.id.status_display1);
		statusDisplay1.setTypeface(font);

		statusDisplay2 = (TextView) findViewById(R.id.status_display2);
		statusDisplay2.setTypeface(font);

		statusDisplay3 = (TextView) findViewById(R.id.status_display3);
		statusDisplay3.setTypeface(font);

		/** user status dialog **/

		// Number of workouts
		numOfWorkouts = (TextView) findViewById(R.id.main_num_of_workouts);
		sessionModel.open();
		numOfWorkouts.setText(" " + sessionModel.getTotal());
		numOfWorkouts.setTypeface(font);

		// Days since last workout
		lastWorkouts = (TextView) findViewById(R.id.main_last_workout);
		Date oldDate;
		boolean noWorkout = false;
		try {
			oldDate = new Date((sessionModel.getMostRecent(null).date_created));
		} catch (Exception e) {
			oldDate = new Date();
			noWorkout = true;
		}

		Date newDate = new Date();
		long sinceLastWorkout = newDate.getTime() - oldDate.getTime();

		// Converting from milliseconds to days
		sinceLastWorkout /= 1000;
		sinceLastWorkout /= 60;
		sinceLastWorkout /= 60;
		sinceLastWorkout /= 24;

		if (noWorkout == false) {
			lastWorkouts.setText(String.valueOf(sinceLastWorkout) + " days");
			lastWorkouts.setTextSize(20);
		} else {
			lastWorkouts.setTextSize(22);
			lastWorkouts.setText("N/A");
		}

		lastWorkouts = (TextView) findViewById(R.id.main_last_workout);

		lastWorkouts.setTypeface(font);

		// Achievements
		achievementModel.open();
		numOfAchievments = (TextView) findViewById(R.id.main_num_of_achievments);
		numOfAchievments.setText(" " + achievementModel.getTotal());
		numOfAchievments.setTypeface(font);
		sessionModel.close();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_button_workouts:
			Intent i = new Intent(this, WorkoutsActivity.class);
			startActivity(i);
			break;

		case R.id.main_button_calendar:
			Intent c = new Intent(this, CalendarActivity.class);
			startActivity(c);
			break;

		case R.id.main_button_profile:
			Intent p = new Intent(this, UserProfileActivity.class);
			startActivity(p);
			break;
		}
	}
}
