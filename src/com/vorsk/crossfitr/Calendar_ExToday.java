package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.TempDB_Darren;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Calendar_ExToday extends Activity implements OnClickListener, OnItemClickListener {
	TempDB_Darren mDBHelper;
	String today;
	Cursor cursor;
	SimpleCursorAdapter adapter;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_extoday);
		
		Intent intent = getIntent();
		today = intent.getStringExtra("Param1");
		
		TextView text = (TextView) findViewById(R.id.texttoday);
		text.setText(today);
		
		mDBHelper = new TempDB_Darren(this, "today.db", null,1);
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		
		cursor = db.rawQuery("SELECT * FROM today WHERE date = '"+ today +"'", null);
		
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] {"title","time"},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		ListView list = (ListView) findViewById(R.id.lv);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		
		mDBHelper.close();
		
		Button btn = (Button) findViewById(R.id.add);
		btn.setOnClickListener(this);
		
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,Calendar_Detail.class);
		cursor.moveToPosition(position);
		intent.putExtra("ParamID",cursor.getInt(0));
		startActivityForResult(intent, 1);
	}
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Calendar_Detail.class);
		intent.putExtra("ParamDate", today);
		startActivityForResult(intent, 0);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 0:
		case 1:
			if(resultCode == RESULT_OK){
				SQLiteDatabase db = mDBHelper.getWritableDatabase();
				cursor = db.rawQuery("SELECT * FROM today WHERE date = '"+ today +"'", null);
				adapter.changeCursor(cursor);
				mDBHelper.close();
			}
			break;
		}
	}
	}
	


