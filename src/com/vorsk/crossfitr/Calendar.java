package com.vorsk.crossfitr;

import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class Calendar extends Activity implements OnClickListener,
		OnItemClickListener {
	/** Called when the activity is first created. */
	ArrayList<String> mItems;
	ArrayAdapter<String> adapter;
	EditText textYear, textMon;
	TextView current_year, current_month;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		textYear = (EditText) findViewById(R.id.yEdit);
		textMon = (EditText) findViewById(R.id.mEdit);
		current_year = (TextView) findViewById(R.id.current_year);
		current_month = (TextView) findViewById(R.id.current_month);

		
		mItems = new ArrayList<String>();

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);

		GridView grid = (GridView) findViewById(R.id.grid1);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);

		Date date = new Date();
		int year = date.getYear() + 1900;
		int mon = date.getMonth() + 1;

		current_year.setText(year + "");
		current_month.setText(mon+"");
		
		textYear.setText(year + "");
		textMon.setText(mon + "");

		fillDate(year, mon);

		Button btnmove = (Button) findViewById(R.id.move);
		btnmove.setOnClickListener(this);
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.move:
			String sYear = textYear.getText().toString();
			String sMon = textMon.getText().toString();
			int year = Integer.valueOf(sYear);
			int mon = Integer.valueOf(sMon);
			fillDate(year, mon);
			break;
		}
	}

	private void fillDate(int year, int mon) {
		mItems.clear();

		mItems.add("Sun");
		mItems.add("Mon");
		mItems.add("Tue");
		mItems.add("Wed");
		mItems.add("Thu");
		mItems.add("Fri");
		mItems.add("Sat");

		Date current = new Date(year - 1900, mon - 1, 1);
		int day = current.getDay();
		// find out a start point
		for (int i = 0; i < day; i++) {
			mItems.add("");
		}
		// commute the last date of months
		current.setDate(32);
		int last = 32 - current.getDate();
		// fill out dates
		for (int i = 1; i <= last; i++) {
			mItems.add(i + "");
		}

		adapter.notifyDataSetChanged();

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		if (mItems.get(position).equals("")) {
			;
		} else {
			Intent intent = new Intent(this, Calendar_ExToday.class);
			intent.putExtra("Param1", textYear.getText().toString() + "/"
					+ textMon.getText().toString() + "/" + mItems.get(position));
			startActivity(intent);
		}
	}
}