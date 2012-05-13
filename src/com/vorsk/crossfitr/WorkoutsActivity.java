package com.vorsk.crossfitr;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class WorkoutsActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_workouts);
        
        View wodButton = findViewById(R.id.wod_button);
        wodButton.setOnClickListener(this);
        View customButton = findViewById(R.id.custom_button);
        customButton.setOnClickListener(this);
        View heroButton = findViewById(R.id.hero_button);
        heroButton.setOnClickListener(this);
        View girlButton = findViewById(R.id.girl_button);
        girlButton.setOnClickListener(this);        
    }
    
    public void onClick(View v)
    {
    	Intent i;
    	switch(v.getId())
    	{
    			
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
}
