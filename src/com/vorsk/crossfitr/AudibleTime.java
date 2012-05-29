package com.vorsk.crossfitr;

import android.media.MediaPlayer;
import android.app.Activity;

public class AudibleTime extends Activity{
	private boolean firstTimeCalled = true;
	MediaPlayer mMediaPlayer = new MediaPlayer();
	
	public boolean playCountdownSound(){
		if(firstTimeCalled){
			mMediaPlayer = MediaPlayer.create(this, R.raw.alarm);
			mMediaPlayer.start();
			firstTimeCalled = false;
			return true;
		}
		return false;
	}
	
	public void playAlarmSound(){
		MediaPlayer media = MediaPlayer.create(this, R.raw.alarm);
		media.start();
	}
}
