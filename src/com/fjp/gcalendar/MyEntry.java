package com.fjp.gcalendar;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;

public class MyEntry implements Comparable {
	CalendarEventEntry entry;
	DateTime startTime;
	DateTime stopTime;
	public MyEntry(CalendarEventEntry ev, DateTime from, DateTime to) {
		entry = ev;
		startTime = from;
		stopTime = to;
	}
	public int compareTo(Object o) {
		MyEntry t = (MyEntry) o;
		return this.getStartTime().compareTo(t.getStartTime());
	}
	public CalendarEventEntry getEntry() {
		return entry;
	}
	public void setEntry(CalendarEventEntry entry) {
		this.entry = entry;
	}
	public DateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	public DateTime getStopTime() {
		return stopTime;
	}
	public void setStopTime(DateTime stopTime) {
		this.stopTime = stopTime;
	}
	
	
	
}
