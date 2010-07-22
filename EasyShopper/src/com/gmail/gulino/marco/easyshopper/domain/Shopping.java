package com.gmail.gulino.marco.easyshopper.domain;

import static android.text.format.DateUtils.FORMAT_24HOUR;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_TIME;
import static android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

import java.util.Calendar;

import android.content.Context;
import android.text.format.DateUtils;

public class Shopping {

	private final long id;
	private Calendar date;
	private Market market;
	
	public Shopping(long id) {
		this(id, null);
	}
	
	public Shopping(long id, Calendar date) {
		this.id = id;
		this.date = date;
	}

	public long getId() {
		return id;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setMarket(Market market) {
		this.market = market;
	}
	
	public Market getMarket() {
		return market;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + " { id=" + getId() + ", date="+ getDate() + ", market=" + market+" }";
	}

	public String formattedDate(Context context) {
		int flags = FORMAT_SHOW_DATE | FORMAT_SHOW_TIME | FORMAT_24HOUR | FORMAT_SHOW_WEEKDAY;
		return DateUtils.formatDateTime(context, getDate().getTimeInMillis(), flags );
	}
}
