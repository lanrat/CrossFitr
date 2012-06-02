package com.vorsk.crossfitr.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.horrabin.horrorss.*;

import android.app.Activity;

public class WODModel {
	public static final String feed_url = "http://feeds.feedburner.com/crossfit/eRTq?format=xml";

	RssParser rss = new RssParser();

	private String title;
	private ArrayList<WorkoutRow> list = new ArrayList<WorkoutRow>();
	SimpleDateFormat formatter;
	WorkoutModel workoutModel;
	public WODModel(Activity activity) {
		workoutModel = new WorkoutModel(activity);
		workoutModel.open();
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
				row.description = android.text.Html.fromHtml(item.getDescription()).toString();
				//row._id = 999999;
				row.workout_type_id = SQLiteDAO.TYPE_WOD;
				row.record = WorkoutModel.NOT_SCORED;
				row.record_type_id = WorkoutModel.SCORE_NONE; 
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
	 * Gets all avaible WODS to display
	 */
	public void fetchAll(){
		this.fetchNew();
		this.fetchDB();
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
