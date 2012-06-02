package com.vorsk.crossfitr;

import java.util.*;
import java.text.*;
import java.sql.Timestamp;

public class DateToTimestamp {
	public static void main(String[] args) {
		try {
			String str_date = "11-June-07";

			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("dd-MMM-yy");
			date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			System.out.println("Today is " + timeStampDate);
		} catch (ParseException e) {
			System.out.println("Exception :" + e);
		}

	}
}