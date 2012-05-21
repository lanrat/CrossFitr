package com.vorsk.crossfitr.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.horrabin.horrorss.*;

public class WODModel {
	public static final String feed_url = "http://feeds.feedburner.com/crossfit/eRTq?format=xml";

	RssParser rss = new RssParser();

	private String title;
	private ArrayList<WorkoutRow> list = new ArrayList<WorkoutRow>();

	public WODModel() {
		
		try{
			RssFeed feed = rss.load(feed_url);
			
			RssChannelBean channel = feed.getChannel();
			this.title = channel.getTitle();
			
			// Gets and iterate the items of the feed 
			List<RssItemBean> items = feed.getItems();
			Iterator<RssItemBean> it = items.iterator();
			
			RssItemBean item;
			WorkoutRow row;
			while (it.hasNext()){
				item = it.next();
				row = new WorkoutRow();
				
				row.name = item.getTitle();
				row.description = item.getDescription();
				row.workout_type_id = SQLiteDAO.TYPE_WOD;
				row.record = WorkoutModel.NOT_SCORED;
				row.record_type_id = WorkoutModel.SCORE_NONE; 
				list.add(row);
			}
			
		}catch(Exception e){
			//TODO  Something to do if an exception occurs
			WorkoutRow row = new WorkoutRow();
			row.name = "Error Loading RSS";
			list.add(row);
		}

	}
	
	public String getTitle(){
		return this.title;
	}
	
	public ArrayList<WorkoutRow> getWodRows(){
		return this.list;
	}

}
