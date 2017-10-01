package com.jdroid.android.share;

public class SharingDataItem {
	
	private String subject;
	private String text;
	
	public SharingDataItem(String text) {
		this.text = text;
	}
	
	public SharingDataItem(String subject, String text) {
		this.subject = subject;
		this.text = text;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
