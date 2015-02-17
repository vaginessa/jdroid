package com.jdroid.android.debug;

import java.util.Date;
import com.jdroid.android.domain.Entity;
import com.jdroid.java.utils.DateUtils;

public class DebugLog extends Entity {
	
	private static final long serialVersionUID = -2732523562518387416L;
	
	private String text;
	private Date dateTime;
	
	public DebugLog() {
	}
	
	public DebugLog(String text) {
		this.text = text;
		dateTime = DateUtils.now();
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
}
