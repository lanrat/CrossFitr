package com.vorsk.crossfitr.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.horrabin.horrorss.*;

import android.app.Activity;
import android.util.Log;

public class WODModel {
	public static final String feed_url = "http://feeds.feedburner.com/crossfit/eRTq?format=xml";

	RssParser rss = new RssParser();
	private static final String TAG = "WODModel";

	private String title;
	private ArrayList<WorkoutRow> list = new ArrayList<WorkoutRow>();
	SimpleDateFormat formatter;
	WorkoutModel workoutModel;
	public WODModel(Activity activity) {
		workoutModel = new WorkoutModel(activity);
		workoutModel.open();
	}
	
	/**
	 * Gets all available WODS to display
	 */
	public void fetchAll(){
		this.fetchNew();
		this.fetchDB();
	}
	
	/**
	 * Run this to get results
	 */
	public void fetchNew(){
		try{
			RssFeed feed = rss.load(feed_url);
			
			RssChannelBean channel = feed.getChannel();
			this.title = channel.getTitle();
			
			// Gets and iterate the items of the feed 
			List<RssItemBean> items = feed.getItems();
			Iterator<RssItemBean> it = items.iterator();
			
			//build the formatter
			formatter = new SimpleDateFormat("EEEE, dd MMM yyyy");
			
			RssItemBean item;
			WorkoutRow row;
			while (it.hasNext()){
				item = it.next();
				row = new WorkoutRow();
				
				row.name = parseTitle(item.getTitle(), item.getPubDate());
				//row.description = "Test";
				row.description = parseDescription(android.text.Html.fromHtml(item.getDescription()).toString());
				//row._id = 999999;
				row.workout_type_id = SQLiteDAO.TYPE_WOD;
				row.record = WorkoutModel.NOT_SCORED;
				row.record_type_id = WorkoutModel.SCORE_TIME;
				//row.record_type_id = WorkoutModel.SCORE_NONE; 
				 if (!list.contains(row)){
					 list.add(row);
				 }
			}
			
		}catch(Exception e){
			//TODO  Something to do if an exception occurs
			WorkoutRow row = new WorkoutRow();
			row.name = "Error Loading RSS";
			list.add(row);
		}
	}
	
	/**
	 * Reduces the junk in the RSS description
	 * @param rss the description to parse
	 * @return a trimmed version of the string
	 */
	private static String parseDescription(String rss){
		int end = rss.indexOf("Enlarge image");
		if (end == -1){
			//could not find substring
			Log.e(TAG,"could not shorten description");
			return rss;
		}
		rss = rss.substring(0, end);
		//try to shorten it again, this will fail on rest days
		int end2 = rss.indexOf("Post ");
		//if it was a rest day
		//trim obj and newline
		if (end2 == -1){
			if (end > 5){
				end -=5;
			}
			return rss.substring(0, end);
		}
		//remove ending newline char
		if (end2 > 2){
			end2 -=2;
		}
		return rss.substring(0, end2);
	}
	
	/**
	 *  adds DB results to internal list
	 */
	public void fetchDB(){
		WorkoutRow[] DBworkouts = workoutModel.getAllByType(WorkoutModel.TYPE_WOD);
	   	 for (int i = 0; i < DBworkouts.length; i++){
			 if (!list.contains(DBworkouts[i])){
				 list.add(DBworkouts[i]);
			 }
		 }
	}
	
	private String parseTitle(String rssTitle, Date pubDate){
		// currently I don't use the actual title...
		return "WOD "+formatter.format(pubDate);
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public ArrayList<WorkoutRow> getWodRows(){
		return this.list;
	}

}
