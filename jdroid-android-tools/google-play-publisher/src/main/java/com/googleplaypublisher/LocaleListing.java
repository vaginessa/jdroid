package com.googleplaypublisher;

import java.io.File;
import java.util.Locale;

public class LocaleListing {
	
	private Locale locale;
	private String listingPath;
	
	private String title;
	private String shortDescription;
	private String fullDescription;
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public String getTitle() {
		File file = new File(listingPath + java.io.File.pathSeparator + locale.toString() + java.io.File.pathSeparator
				+ "details/title.txt");
		return title;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public String getFullDescription() {
		return fullDescription;
	}
}
