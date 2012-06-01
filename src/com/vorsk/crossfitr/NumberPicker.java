/*
 * Copyright 2008 The Android Open Source Project
 * Copyright 2011-2012 Michael Novak <michael.novakjr@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vorsk.crossfitr;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView.OnEditorActionListener;

/**
 * This class has been pulled from the Android platform source code, its an internal widget that hasn't been
 * made public so its included in the project in this fashion for use with the preferences screen; I have made
 * a few slight modifications to the code here, I simply put a MAX and MIN default in the code but these values
 * can still be set publicly by calling code.
 *
 * @author Google
 */
public class NumberPicker extends LinearLayout implements OnClickListener,
        OnEditorActionListener, OnFocusChangeListener, OnLongClickListener {

  //  private static final String TAG = "NumberPicker";
    private static final int DEFAULT_MAX = 59;
    private static final int DEFAULT_MAX_HOUR = 9;
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_VALUE = 0;
    private static final boolean DEFAULT_WRAP = true;

    public interface OnChangedListener {
        void onChanged(NumberPicker picker, int oldVal, int newVal);
    }

    public interface Formatter {
        String toString(int value);
    }

    /*
     * Use a custom NumberPicker formatting callback to use two-digit
     * minutes strings like "01".  Keeping a static formatter etc. is the
     * most efficient way to do this; it avoids creating temporary objects
     * on every call to format().
     */
    public static final NumberPicker.Formatter TWO_DIGIT_FORMATTER =
            new NumberPicker.Formatter() {
                final StringBuilder mBuilder = new StringBuilder();
                final java.util.Formatter mFmt = new java.util.Formatter(mBuilder);
                final Object[] mArgs = new Object[1];
                public String toString(int value) {
                    mArgs[0] = value;
                    mBuilder.delete(0, mBuilder.length());
                    mFmt.format("%02d", mArgs);
                    return mFmt.toString();
                }
            };

    private final Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (mIncrementSec) {
                changeCurrentSec(mCurrentSec + 1);
                mHandler.postDelayed(this, mSpeed);
            } 
            else if (mDecrementSec) {
                changeCurrentSec(mCurrentSec - 1);
                mHandler.postDelayed(this, mSpeed);
            }
            if (mIncrementMin) {
                changeCurrentMin(mCurrentMin + 1);
                mHandler.postDelayed(this, mSpeed);
            } 
            else if (mDecrementMin) {
                changeCurrentMin(mCurrentMin - 1);
                mHandler.postDelayed(this, mSpeed);
            } 
            if (mIncrementHour) {
                changeCurrentHour(mCurrentHour + 1);
                mHandler.postDelayed(this, mSpeed);
            } 
            else if (mDecrementHour) {
                changeCurrentHour(mCurrentHour - 1);
                mHandler.postDelayed(this, mSpeed);
            }
        }
    };

    private final EditText mTextSec;
    private final EditText mTextMin;
    private final EditText mTextHour;

    private String[] mDisplayedValues;
    protected int mStart;
    protected int mEnd;
    protected int mEndHour;
    protected int mCurrentMin;
    protected int mCurrentSec;
    protected int mCurrentHour;
    
    protected int mPreviousSec;
    protected int mPreviousMin;
    protected int mPreviousHour;
    protected OnChangedListener mListenerSec;
    protected OnChangedListener mListenerMin;
    protected OnChangedListener mListenerHour;
    protected Formatter mFormatter;
    protected boolean mWrap;
    protected long mSpeed = 100;

    private boolean mIncrementSec;
    private boolean mDecrementSec;
    private boolean mIncrementMin;
    private boolean mDecrementMin;
    private boolean mIncrementHour;
    private boolean mDecrementHour;

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.number_picker, this, true);
        mHandler = new Handler();
     
        mIncrementSecButton = (NumberPickerButton) findViewById(R.id.increment_sec);
        mIncrementSecButton.setOnClickListener(this);
        mIncrementSecButton.setOnLongClickListener(this);
        mIncrementSecButton.setNumberPicker(this);
        mDecrementSecButton = (NumberPickerButton) findViewById(R.id.decrement_sec);
        mDecrementSecButton.setOnClickListener(this);
        mDecrementSecButton.setOnLongClickListener(this);
        mDecrementSecButton.setNumberPicker(this);

        mIncrementMinButton = (NumberPickerButton) findViewById(R.id.increment_min);
        mIncrementMinButton.setOnClickListener(this);
        mIncrementMinButton.setOnLongClickListener(this);
        mIncrementMinButton.setNumberPicker(this);
        mDecrementMinButton = (NumberPickerButton) findViewById(R.id.decrement_min);
        mDecrementMinButton.setOnClickListener(this);
        mDecrementMinButton.setOnLongClickListener(this);
        mDecrementMinButton.setNumberPicker(this);
        
        mIncrementHourButton = (NumberPickerButton) findViewById(R.id.increment_hour);
        mIncrementHourButton.setOnClickListener(this);
        mIncrementHourButton.setOnLongClickListener(this);
        mIncrementHourButton.setNumberPicker(this);
        mDecrementHourButton = (NumberPickerButton) findViewById(R.id.decrement_hour);
        mDecrementHourButton.setOnClickListener(this);
        mDecrementHourButton.setOnLongClickListener(this);
        mDecrementHourButton.setNumberPicker(this);

        mTextSec = (EditText) findViewById(R.id.timepicker_input_sec);
        mTextSec.setOnFocusChangeListener(this);
        mTextSec.setOnEditorActionListener(this);
        mTextSec.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mTextSec.setInputType(0); //See http://code.google.com/p/android/issues/detail?id=7115
        mTextSec.setEnabled(true); 
        
        mTextMin = (EditText) findViewById(R.id.timepicker_input_min);
        mTextMin.setOnFocusChangeListener(this);
        mTextMin.setOnEditorActionListener(this);
        mTextMin.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mTextMin.setInputType(0); //See http://code.google.com/p/android/issues/detail?id=7115
        mTextMin.setEnabled(true); 
        
        mTextHour = (EditText) findViewById(R.id.timepicker_input_hour);
        mTextHour.setOnFocusChangeListener(this);
        mTextHour.setOnEditorActionListener(this);
        mTextHour.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mTextHour.setInputType(0); //See http://code.google.com/p/android/issues/detail?id=7115
        mTextHour.setEnabled(true); 

        if (!isEnabled()) {
            setEnabled(false);
        }

        TypedArray a = context.obtainStyledAttributes( attrs, R.styleable.numberpicker );
        mStart = a.getInt( R.styleable.numberpicker_startRange, DEFAULT_MIN );
        mEnd = a.getInt( R.styleable.numberpicker_endRange, DEFAULT_MAX );
        mEndHour = a.getInt( R.styleable.numberpicker_endRange, DEFAULT_MAX_HOUR );
        mWrap = a.getBoolean( R.styleable.numberpicker_wrap, DEFAULT_WRAP );
        
        mCurrentSec = a.getInt( R.styleable.numberpicker_defaultValue, DEFAULT_VALUE );
        mCurrentSec = Math.max( mStart, Math.min( mCurrentSec, mEnd ) );
        mCurrentMin = a.getInt( R.styleable.numberpicker_defaultValue, DEFAULT_VALUE );
        mCurrentMin = Math.max( mStart, Math.min( mCurrentMin, mEnd ) );
        mCurrentHour = a.getInt( R.styleable.numberpicker_defaultValue, DEFAULT_VALUE );
        mCurrentHour = Math.max( mStart, Math.min( mCurrentHour, mEndHour ) );
        
        mTextSec.setText( "" + mCurrentSec);
        mTextMin.setText( "" + mCurrentMin);
        mTextHour.setText( "" + mCurrentHour );
    }

    private String pad(String input) {
	    if (Integer.parseInt(input) >= 10)
	        return String.valueOf(Integer.parseInt(input));
	    else
	        return "0" + String.valueOf(Integer.parseInt(input));
	}
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mIncrementSecButton.setEnabled(enabled);
        mDecrementSecButton.setEnabled(enabled);
        mIncrementMinButton.setEnabled(enabled);
        mDecrementMinButton.setEnabled(enabled);
        mIncrementHourButton.setEnabled(enabled);
        mDecrementHourButton.setEnabled(enabled);
        mTextSec.setEnabled(false);
        mTextMin.setEnabled(false);
        mTextHour.setEnabled(false);
    }

    public void setOnChangeListenerSec(OnChangedListener listener) {
        mListenerSec = listener;
    }
    
    public void setOnChangeListenerMin(OnChangedListener listener) {
        mListenerMin = listener;
    }
 
    public void setOnChangeListenerHour(OnChangedListener listener) {
        mListenerHour = listener;
    }
    

    public void setFormatter(Formatter formatter) {
        mFormatter = formatter;
    }

    /**
     * Set the range of numbers allowed for the number picker. The current
     * value will be automatically set to the start.
     *
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     * @param end the end of the hour range (inclusive)
     */
    public void setRange(int start, int end, int endHour) {
        mStart = start;
        mEnd = end;
        mEndHour = endHour; 
        mCurrentMin = start;
        mCurrentSec = start;
        mCurrentHour = start;
        updateViewSec();
        updateViewMin();
        updateViewHour();
    }

    /**
     * Specify if numbers should wrap after the edge has been reached.
     *
     * @param wrap values
     */
    public void setWrap( boolean wrap ) {
        mWrap = wrap;
    }

    /**
     * Set the range of numbers allowed for the number picker. The current
     * value will be automatically set to the start. Also provide a mapping
     * for values used to display to the user.
     *
     * @param start the start of the range (inclusive)
     * @param end the end of the range (inclusive)
     * @param end the end of the hour range (inclusive)
     * @param displayedValues the values displayed to the user.
     */
    public void setRange(int start, int end, int endHour, String[] displayedValues) {
        mDisplayedValues = displayedValues;
        mStart = start;
        mEnd = end;
        mEndHour = endHour;
        mCurrentMin = start;
        mCurrentSec = start;
        mCurrentHour = start;
        updateViewSec();
        updateViewMin();
        updateViewHour();
    }

    public void setCurrentSec(int current) {
        if (mEnd < current) throw new IllegalArgumentException("Current value cannot be greater than the range end.");
        mCurrentSec = current;
        updateViewSec();
    }

    public void setCurrentMin(int current) {
        if (mEnd < current) throw new IllegalArgumentException("Current value cannot be greater than the range end.");
        mCurrentMin = current;
        updateViewMin();
    }
    
    public void setCurrentHour(int current) {
        if (mEndHour < current) throw new IllegalArgumentException("Current value cannot be greater than the range end.");
        mCurrentHour = current;
        updateViewHour();
    }
    
    public void setCurrentSecAndNotify(int current) {
        mCurrentSec = current;
        notifyChangeSec();
        updateViewSec();
    }
    
    public void setCurrentMinAndNotify(int current) {
        mCurrentMin = current;
        notifyChangeMin();
        updateViewMin();
    }
    
    public void setCurrentHourAndNotify(int current) {
        mCurrentHour = current;
        notifyChangeHour();
        updateViewHour();
    }


    /**
     * The speed (in milliseconds) at which the numbers will scroll
     * when the the +/- buttons are long-pressed. Default is 300ms.
     */
    public void setSpeed(long speed) {
        mSpeed = speed;
    }

    public void onClick(View v) {
        // now perform the increment/decrement
        if (R.id.increment_sec == v.getId()) 
        {
        	 validateInputSec(mTextSec);
        	 if (!mTextSec.hasFocus()) mTextSec.requestFocus();
            changeCurrentSec(mCurrentSec + 1);
        } 
        
        else if (R.id.decrement_sec == v.getId()) 
        {
        	validateInputSec(mTextSec);
       	    if (!mTextSec.hasFocus()){
       	    	mTextSec.requestFocus();
       	    }
            changeCurrentSec(mCurrentSec - 1);
        }
        
        else if (R.id.increment_min == v.getId()) {
        	 validateInputMin(mTextMin);
        	 if (!mTextMin.hasFocus()){
        		 mTextMin.requestFocus();
        	 }
            changeCurrentMin(mCurrentMin + 1);
        } 
        
        else if (R.id.decrement_min == v.getId()) {
        	validateInputMin(mTextMin);
       	 	if (!mTextMin.hasFocus()){
       	 		mTextMin.requestFocus();
       	 	}
            changeCurrentMin(mCurrentMin - 1);
        }
        else if (R.id.increment_hour == v.getId()) {
        	validateInputHour(mTextHour);
        	if (!mTextHour.hasFocus()){
        		mTextHour.requestFocus();
        	}
            changeCurrentHour(mCurrentHour + 1);
            
        } 
        else if (R.id.decrement_hour == v.getId()) {
        	validateInputHour(mTextHour);
        	if (!mTextHour.hasFocus()){
        		mTextHour.requestFocus();
        	}
            changeCurrentHour(mCurrentHour - 1);
        }
    }

    protected String formatNumber(int value) {
        return (mFormatter != null) ? mFormatter.toString(value) : String.valueOf(value);
    }

    protected void changeCurrentSec(int current) {
        // Wrap around the values if we go past the start or end
        if (current > mEnd) {
            current = mWrap ? mStart : mEnd;
        } else if (current < mStart) {
            current = mWrap ? mEnd : mStart;
        }
        mPreviousSec = mCurrentSec;
        mCurrentSec = current;
        
        notifyChangeSec();
        updateViewSec();
    }
  
    protected void changeCurrentMin(int current) {
        // Wrap around the values if we go past the start or end
        if (current > mEnd) {
            current = mWrap ? mStart : mEnd;
        } else if (current < mStart) {
            current = mWrap ? mEnd : mStart;
        }
        mPreviousMin = mCurrentMin;
        mCurrentMin = current;
        
        notifyChangeMin();
        updateViewMin();
    }

    protected void changeCurrentHour(int current) {
        // Wrap around the values if we go past the start or end
        if (current > mEndHour) {
            current = mWrap ? mStart : mEndHour;
        } else if (current < mStart) {
            current = mWrap ? mEndHour : mStart;
        }
        mPreviousHour = mCurrentHour;
        mCurrentHour = current;
        
        notifyChangeHour();
        updateViewHour();
    }

    protected void notifyChangeSec() {
        if (mListenerSec != null) {
            mListenerSec.onChanged(this, mPreviousSec, mCurrentSec);
        }
    }
    
    protected void notifyChangeMin() {
        if (mListenerMin != null) {
            mListenerMin.onChanged(this, mPreviousMin, mCurrentMin);
        }
    }
    
    protected void notifyChangeHour() {
        if (mListenerHour != null) {
            mListenerHour.onChanged(this, mPreviousHour, mCurrentHour);
        }
    }

    protected void updateViewSec() {
        if (mDisplayedValues == null) {
            mTextSec.setText(pad(formatNumber(mCurrentSec)));
        } 
        else {
            mTextSec.setText(mDisplayedValues[mCurrentSec - mStart]);  
        }
        mTextSec.setSelection(mTextSec.getText().length());
    }

    protected void updateViewMin() {
        if (mDisplayedValues == null) 
            mTextMin.setText(pad(formatNumber(mCurrentMin))); 
        else
            mTextMin.setText(mDisplayedValues[mCurrentMin - mStart]); 
        mTextMin.setSelection(mTextMin.getText().length());
    }

    protected void updateViewHour() {
        if (mDisplayedValues == null) {
            mTextHour.setText(formatNumber(mCurrentHour));
        } 
        else {
            mTextHour.setText(mDisplayedValues[mCurrentHour - mStart]);
        }
        mTextHour.setSelection(mTextHour.getText().length());
    }

    private void validateCurrentViewSec(CharSequence str) {
        int val = getSelectedPos(str.toString());
        if ((val >= mStart) && (val <= mEnd)) {
            if (mCurrentSec != val) {
                mPreviousSec = mCurrentSec;
                mCurrentSec = val;
                notifyChangeSec();
            }
        }
        updateViewSec();
    }
    
    private void validateCurrentViewMin(CharSequence str) {
        int val = getSelectedPos(str.toString());
        if ((val >= mStart) && (val <= mEnd)) {
            if (mCurrentMin != val) {
                mPreviousMin = mCurrentMin;
                mCurrentMin = val;
                notifyChangeMin();
            }
        }
        updateViewMin();
    }
    
    private void validateCurrentViewHour(CharSequence str) {
        int val = getSelectedPos(str.toString());
        if ((val >= mStart) && (val <= mEndHour)) {
           if (mCurrentHour != val) {
                mPreviousHour = mCurrentHour;
                mCurrentHour = val;
                notifyChangeHour();
            }
        }
        updateViewHour();
    }

    /**
     * Do nothing
     */
    public void onFocusChange(View v, boolean hasFocus) { }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == mTextSec) {
            validateInputSec(v);
            // Don't return true, let Android handle the soft keyboard
        }
        if (v == mTextMin) {
            validateInputMin(v);
            // Don't return true, let Android handle the soft keyboard
        }
        if (v == mTextHour) {
            validateInputHour(v);
            // Don't return true, let Android handle the soft keyboard
        }
        return false;
    }

    private void validateInputSec(View v) {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str)) {

            // Restore to the old value as we don't allow empty values
            updateViewSec();
        } else {

            // Check the new value and ensure it's in range
            validateCurrentViewSec(str);
        }
    }

    private void validateInputMin(View v) {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str)) {

            // Restore to the old value as we don't allow empty values
            updateViewMin();
        } else {

            // Check the new value and ensure it's in range
            validateCurrentViewMin(str);
        }
    }
    private void validateInputHour(View v) {
        String str = String.valueOf(((TextView) v).getText());
        if ("".equals(str)) {

            // Restore to the old value as we don't allow empty values
            updateViewHour();
        } else {

            // Check the new value and ensure it's in range
            validateCurrentViewHour(str);
        }
    }
    /**
     * We start the long click here but rely on the {@link NumberPickerButton}
     * to inform us when the long click has ended.
     */
	public boolean onLongClick(View v) {
		if (R.id.increment_sec == v.getId() ||
				R.id.decrement_sec == v.getId()){
			onLongClickSec(v);
		}
		else if(R.id.increment_min == v.getId() ||
				R.id.decrement_min == v.getId()){
			onLongClickMin(v);
		}
		
		else if(R.id.increment_hour == v.getId() ||
				R.id.decrement_hour == v.getId()){
			onLongClickHour(v);
		}
		return false;
	}
	
    public boolean onLongClickSec(View v) {

        /* The text view may still have focus so clear it's focus which will
         * trigger the on focus changed and any typed values to be pulled.
         */
       mTextSec.clearFocus();
       mTextSec.requestFocus();
     
        if (R.id.increment_sec == v.getId()) {
            mIncrementSec = true;
            mHandler.post(mRunnable);
        } 
        else if (R.id.decrement_sec == v.getId()) {
            mDecrementSec = true;
            mHandler.post(mRunnable);
        }
        return true;
    }

    public boolean onLongClickMin(View v) {

        /* The text view may still have focus so clear it's focus which will
         * trigger the on focus changed and any typed values to be pulled.
         */    
        mTextMin.clearFocus();
        mTextMin.requestFocus();
        if (R.id.increment_min == v.getId()) {
            mIncrementMin = true;
            mHandler.post(mRunnable);
        } 
        else if (R.id.decrement_min == v.getId()) {
            mDecrementMin = true;
            mHandler.post(mRunnable);
        }
        
        return true;
    }
    
    public boolean onLongClickHour(View v) {

        /* The text view may still have focus so clear it's focus which will
         * trigger the on focus changed and any typed values to be pulled.
         */
        mTextHour.clearFocus();
        mTextHour.requestFocus();
        if (R.id.increment_hour == v.getId()) {
            mIncrementHour = true;
            mHandler.post(mRunnable);
        } 
        else if (R.id.decrement_hour == v.getId()) {
            mDecrementHour = true;
            mHandler.post(mRunnable);
        }
        return true;
    }
    
    public void cancelIncrement() {
        mIncrementSec = false;
        mIncrementMin = false;
        mIncrementHour = false;
    }

    public void cancelDecrement() {
        mDecrementSec = false;
        mDecrementMin = false;
        mDecrementHour = false;
    }


    private NumberPickerButton mIncrementSecButton;
    private NumberPickerButton mDecrementSecButton;
    private NumberPickerButton mIncrementMinButton;
    private NumberPickerButton mDecrementMinButton;
    private NumberPickerButton mIncrementHourButton;
    private NumberPickerButton mDecrementHourButton;

    private int getSelectedPos(String str) {
        if (mDisplayedValues == null) {
            return Integer.parseInt(str);
        } else {
            for (int i = 0; i < mDisplayedValues.length; i++) {

                /* Don't force the user to type in jan when ja will do */
                str = str.toLowerCase();
                if (mDisplayedValues[i].toLowerCase().startsWith(str)) {
                    return mStart + i;
                }
            }

            /* The user might have typed in a number into the month field i.e.
             * 10 instead of OCT so support that too.
             */
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {

                /* Ignore as if it's not a number we don't care */
            }
        }
        return mStart;
    }

    /**
     * @return the current value.
     */
    public int getCurrentSec() {
        return mCurrentSec;
    }
    
    public int getCurrentMin() {
        return mCurrentMin;
    }
    
    public int getCurrentHour() {
        return mCurrentHour;
    }
}
