package com.vorsk.crossfitr;

public class StopWatch {
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
	 */
	public enum State { PAUSED, RUNNING };

	private GetTime time;
	private long startTime;
	private long stopTime;
	private long pauseOffset;

	private State runState;

	public StopWatch() {
		time = SystemTime;
		reset();
	}
	public StopWatch(GetTime sTime) {
		time = sTime;
		reset();
	}

	/**
	 * Start the stopwatch running. If the stopwatch is already running, this
	 * does nothing. 
	 */
	public void start() {
		if ( runState == State.PAUSED ) {
			pauseOffset = getElapsedTime();
			stopTime = 0;
			startTime = time.now();
			runState = State.RUNNING;
		}
	}

	/***
	 * Pause the stopwatch. If the stopwatch is already running, do nothing.
	 */
	public void pause() {
		if ( runState == State.RUNNING ) {
			stopTime = time.now();
			runState = State.PAUSED;
		}
	}

	/**
	 * Reset the stopwatch to the initial state, clearing all stored times. 
	 */
	public void reset() {
		runState = State.PAUSED;
		startTime = 0;
		stopTime = 0;
		pauseOffset	= 0;
	}

	/***
	 * @return The amount of time recorded by the stopwatch, in milliseconds
	 */
	public long getElapsedTime() {
		if ( runState == State.PAUSED ) {
			return (stopTime - startTime) + pauseOffset;
		} else {
			return (time.now() - startTime) + pauseOffset;
		}
	}


	/**
	 * @return true if the stopwatch is currently running and recording
	 * 		   time, false otherwise.
	 */
	public boolean isRunning() {
		return (runState == State.RUNNING);
	}
}
