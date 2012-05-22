package com.vorsk.crossfitr;

import com.vorsk.crossfitr.models.TempDB_Darren;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Calendar_Detail extends Activity implements OnClickListener {
	TempDB_Darren mDBHelper;
	int mId;
	String today;
	EditText editDate, editTitle, editTime, editMemo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_detail);
		
		editDate = (EditText)findViewById(R.id.day);
		editTitle = (EditText)findViewById(R.id.head);
		editTime = (EditText)findViewById(R.id.time);
		editMemo = (EditText)findViewById(R.id.memo);
		
		Intent intent = getIntent();
		mId = intent.getIntExtra("ParamID", -1);
		today = intent.getStringExtra("ParamDate");
		
		mDBHelper = new TempDB_Darren(this, "today.db", null, 1);
		
		if(mId == -1){
			editDate.setText(today);
		}else{
			SQLiteDatabase db = mDBHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM today WHERE _id='"+mId+"'", null);
			startManagingCursor(cursor);
			if(cursor.moveToNext()){
				editTitle.setText(cursor.getString(1));
				editDate.setText(cursor.getString(2));
				editTime.setText(cursor.getString(3));
				editMemo.setText(cursor.getString(4));
			}
			mDBHelper.close();
					
		}
		Button bbtn1 = (Button) findViewById(R.id.btn1);
		Button bbtn2 = (Button) findViewById(R.id.btn2);
		Button bbtn3 = (Button) findViewById(R.id.btn3);
		bbtn1.setOnClickListener(this);
		bbtn2.setOnClickListener(this);
		bbtn3.setOnClickListener(this);
		
		if(mId == -1){
			bbtn2.setVisibility(View.INVISIBLE);
		}
	}
	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		db=mDBHelper.getWritableDatabase();
				
		switch(arg0.getId()){
		case R.id.btn1:
			if(mId != -1){
				db.execSQL("UPDATE today SET title='" 
				+ editTitle.getText().toString() 
				+ "',date='"+ editDate.getText().toString()
				+"', time='" + editTime.getText().toString()
				+"', memo='"+ editMemo.getText().toString()
				+"'WHERE _id='"+ mId+"';");
			}else{
				db.execSQL("INSERT INTO today VALUES(null, '" 
				+ editTitle.getText().toString() 
				+ "', '"
				+ editDate.getText().toString()
				+"', '" 
				+ editTime.getText().toString()
				+"', '"
				+ editMemo.getText().toString()
				+"');");
			}
			mDBHelper.close();
			setResult(RESULT_OK);
			break;
		case R.id.btn2:
			if(mId != -1){
				db.execSQL("DELETE FROM today WHERE _id='"+ mId + "';");
				mDBHelper.close();
			}
			setResult(RESULT_OK);
			break;
		case R.id.btn3:
			setResult(RESULT_CANCELED);
			break;
		}
		finish();
	}
	
	
}
