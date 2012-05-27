package com.vorsk.crossfitr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class StopwatchActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("This is the Stopwatch tab");
		setContentView(textview);
	}
}
