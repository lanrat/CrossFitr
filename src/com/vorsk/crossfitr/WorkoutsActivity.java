package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WorkoutsActivity extends Activity implements OnClickListener {
	private TextView titleText;
	
	private TextView wodText;
	private TextView heroText;
	private TextView girlText;
	private TextView customText;
	private Typeface font;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workouts);
		
		font = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Thin.ttf");

		titleText = (TextView) findViewById(R.id.workouts_title);
		titleText.setTypeface(font);

		View wodButton = findViewById(R.id.wod_button);
		wodButton.setOnClickListener(this);
		wodText = (TextView) findViewById(R.id.wod_button);
		wodText.setTypeface(font);
		
		View customButton = findViewById(R.id.custom_button);
		customButton.setOnClickListener(this);
		customText = (TextView) findViewById(R.id.custom_button);
		customText.setTypeface(font);
		
		View heroButton = findViewById(R.id.hero_button);
		heroButton.setOnClickListener(this);
		heroText = (TextView) findViewById(R.id.hero_button);
		heroText.setTypeface(font);
		
		
		View girlButton = findViewById(R.id.girl_button);
		girlButton.setOnClickListener(this);
		girlText = (TextView) findViewById(R.id.girl_button);
		girlText.setTypeface(font);

	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.wod_button:
			i = new Intent(this, WodActivity.class);
			startActivity(i);
			break;

		case R.id.girl_button:
			i = new Intent(this, GirlsActivity.class);
			startActivity(i);
			break;

		case R.id.hero_button:
			i = new Intent(this, HeroesActivity.class);
			startActivity(i);
			break;

		case R.id.custom_button:
			i = new Intent(this, CustomActivity.class);
			startActivity(i);
			break;
		}
	}
	
	public void onBackPressed() {
		Intent u = new Intent(this, CrossFitrActivity.class);
		startActivity(u);
	}
}
