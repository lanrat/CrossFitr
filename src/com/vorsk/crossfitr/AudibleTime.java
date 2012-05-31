package com.vorsk.crossfitr;

import android.media.MediaPlayer;
import android.app.Activity;

public class AudibleTime extends Activity{
	
	public void playCountdownSound(){
		MediaPlayer media = MediaPlayer.create(this,R.raw.countdown_3_0);
		media.start();
		

	}
	
	public void playAlarmSound(){
		MediaPlayer media = MediaPlayer.create(this, R.raw.alarm);
		media.start();
	}
}
