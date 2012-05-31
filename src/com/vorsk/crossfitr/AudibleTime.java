package com.vorsk.crossfitr;

import android.media.MediaPlayer;
import android.app.Activity;

public class AudibleTime extends Activity{
	MediaPlayer mMediaPlayer = new MediaPlayer();
	
	public void playCountdownSound(){
		mMediaPlayer = MediaPlayer.create(this, R.raw.alarm);
		mMediaPlayer.start();

	}
	
	public void playAlarmSound(){
		MediaPlayer media = MediaPlayer.create(this, R.raw.alarm);
		media.start();
	}
}
