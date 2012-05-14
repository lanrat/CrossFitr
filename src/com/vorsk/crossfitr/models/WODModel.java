package com.vorsk.crossfitr.models;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class WODModel {
	public static final String feed_url = "http://feeds.feedburner.com/crossfit/eRTq?format=xml";
	private URL url;
	
	//TODO: rm
    public ArrayList<String> list = new ArrayList<String>();

	public WODModel() {
		RootElement root = new RootElement("rss");
        Element itemlist = root.getChild("channel");
        Element item =  itemlist.getChild("item");
               
        item.getChild("title").setEndTextElementListener(new EndTextElementListener(){
                public void end(String body) {
                        list.add(body);
                }
        });
               
        try {
                url = new URL(feed_url);
                InputStream input = url.openConnection().getInputStream();
                Xml.parse(input, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
                Log.e("RSSParser",e.toString());
        }
        
    }

}
