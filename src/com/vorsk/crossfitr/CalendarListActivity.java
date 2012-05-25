package com.vorsk.crossfitr;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarListActivity extends ListActivity {	
	@Override
	public void onCreate(Bundle savedInstanceState){
		setListAdapter(new ArrayAdapter<String>(this, R.id.calendar_list, COUNTRIES));
		
		ListView recordList = getListView();
		recordList.setTextFilterEnabled(true);
		
		recordList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
					 Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
							 Toast.LENGTH_SHORT).show();
	      
      }
		});
		
	}
	
	static final String[] COUNTRIES = new String[] {    "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",    
		"Angola", "Anguilla", "Antarctica", 
		"Antigua and Barbuda", "Argentina",
		"Armenia", "Aruba", "Australia", "Austria", 
		"Azerbaijan",    "Bahrain", "Bangladesh", "Barbados", 
		"Belarus", "Belgium"};
	
	

}
