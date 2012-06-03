package com.vorsk.crossfitr;

import android.util.Log;

public class Time {

	/**
	 * Implements a method that returns the current time, in milliseconds.
	 * Used for testing
	 */
	public interface GetTime {
		public long now();
	}

	/**
	 * Default way to get time. Just use the system clock.
	 */
	private GetTime SystemTime = new GetTime() {

		public long now() {	return System.currentTimeMillis(); }
	};

	/**
	 * What is the stopwatch doing?
	 * we can use state if we have several boolean later
	 */
	//public enum State { PAUSED, RUNNING };
	
	private boolean running = false;
	private static String TAG = "Time";
	private GetTime m_time;
	private long m_startTime;
	private long m_stopTime;
	private long m_pauseOffset;
	//private State m_state;

	public Time() {
		m_time = SystemTime;
	}
	public Time(GetTime time) {
		m_time = time;
	}

	/**
	 * Start the stopwatch running. If the stopwatch is already running, this
	 * does nothing. 
	 */
	public void start() {
		Log.d(TAG, "start");
		if ( running == false ) {
			m_pauseOffset = getElapsedTime();
			m_stopTime = 0;
			m_startTime = m_time.now();
			running = true;
		}
	}

	/***
	 * Pause the stopwatch. If the stopwatch is already running, do nothing.
	 */
	public void stop() {
		Log.d(TAG, "stop");
		if ( running == true ) {
			m_stopTime = m_time.now();
			running = false;
		}
	}

	/**
	 * Reset the stopwatch to the initial state, clearing all stored times. 
	 */
	public void reset() {
		Log.d(TAG, "reset");
		running = false;
		m_startTime 	= 0;
		m_stopTime 		= 0;
		m_pauseOffset 	= 0;
	}


	/***
	 * @return The amount of time recorded by the stopwatch, in milliseconds
	 */
	public long getElapsedTime() {
		if ( running == false ) {
			return (m_stopTime - m_startTime) + m_pauseOffset;
		} else {
			return (m_time.now() - m_startTime) + m_pauseOffset;
		}
	}


	/**
	 * @return true if the stopwatch is currently running and recording
	 * 		   time, false otherwise.
	 */
	public boolean isRunning() {
		return running;
	}
}
